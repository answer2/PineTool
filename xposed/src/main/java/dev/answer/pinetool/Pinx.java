/*
 * Copyright (C) 2024 AnswerDev
 * Licensed under the GNU General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by AnswerDev
 */
package dev.answer.pinetool;
import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import dev.answer.pinetool.callback.XC_AfterCallback;
import dev.answer.pinetool.callback.XC_BeforeCallback;
import dev.answer.pinetool.callback.XC_ReplaceCallback;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;

public class Pinx {
    
    private static final String TAG = "Pinx";
    
    // Check Debug
    private static boolean isDebug = false;

    // Maps to store method information, hooks, and replacements
    private static final Map<Member, MethodInfo> methodMaps = new HashMap<>();
    private static final Map<Member, HookCallback> hookMaps = new HashMap<>();
    private static final Map<Member, ReplacementCallback> replaceMaps = new HashMap<>();
    private static final Map<Member, XC_MethodHook.Unhook> unhookMaps = new HashMap<>();

    public static final XC_ReplaceCallback DO_NOTHING = param-> null;
    
    /**
     * Adds an after hook to the specified method.
     *
     * @param member   The method to hook.
     * @param callback The callback to execute after the method is called.
     */
    public static void after(Member member, XC_AfterCallback callback) {
        if (member == null) {
            Log.e(TAG, "member can't be null");
            return;
        }
        
        MethodInfo info = methodMaps.computeIfAbsent(member, k -> new MethodInfo());
        if (info.replace == null) {
            info.setAfter(callback);
            if (!hookMaps.containsKey(member)) {
                HookCallback hook = new HookCallback(member);
                hookMaps.put(member, hook);
                unhookMaps.put(member, XposedBridge.hookMethod(member, hook));
            }
        }
    }

    /**
     * Adds a before hook to the specified method.
     *
     * @param member   The method to hook.
     * @param callback The callback to execute before the method is called.
     */
    public static void before(Member member, XC_BeforeCallback callback) {
        if (member == null) {
            Log.e(TAG, "member can't be null");
            return;
        }

        MethodInfo info = methodMaps.computeIfAbsent(member, k -> new MethodInfo());
        if (info.replace == null) {
            info.setBefore(callback);
            if (!hookMaps.containsKey(member)) {
                HookCallback hook = new HookCallback(member);
                hookMaps.put(member, hook);
                unhookMaps.put(member, XposedBridge.hookMethod(member, hook));
            }
        }
    }

    /**
     * Replaces the specified method with the provided callback.
     *
     * @param member   The method to replace.
     * @param callback The callback that replaces the method.
     */
    public static void replace(Member member, XC_ReplaceCallback callback) {
        if (member == null) {
            Log.e(TAG, "member can't be null");
            return;
        }

        MethodInfo info = new MethodInfo().setReplace(callback);
        methodMaps.put(member, info);
        if (!replaceMaps.containsKey(member)) {
            ReplacementCallback replace = new ReplacementCallback(member);
            replaceMaps.put(member, replace);
            unhookMaps.put(member, XposedBridge.hookMethod(member, replace));
        }
    }

  /**
   * Replaces the implementation of the provided `member` with a "do nothing" method.
   *
   * <p>This method is primarily used when you want to neutralize a method call by replacing it with
   * a no-op (a method that does nothing). If the `member` is not already in `replaceMaps`, a new
   * `ReplacementCallback` is created and hooked. This callback is stored in `replaceMaps` to ensure
   * the replacement only happens once for each member.
   *
   * <p>The reason for creating a `ReplacementCallback` instead of using a simple placeholder is to
   * facilitate the ability to unhook the replacement later. This allows for restoring the original
   * method implementation if needed.
   *
   * @param member The method or constructor to be replaced with a "do nothing" implementation.
   */
  public static void doNothing(Member member) {
        if (member == null) {
            Log.e(TAG, "member can't be null");
            return;
        }

        MethodInfo info = new MethodInfo().setReplace(DO_NOTHING);
        methodMaps.put(member, info);
        if (!replaceMaps.containsKey(member)) {
            ReplacementCallback replace = new ReplacementCallback(member);
            replaceMaps.put(member, replace);
            unhookMaps.put(member, XposedBridge.hookMethod(member, replace));
        }
    }

    /**
     * Unhooks a method by removing its associated hook from the map and calling the unhook method.
     *
     * @param member The method or constructor to unhook.
     */
    public static void unHook(Member member) {
        if (unhookMaps.containsKey(member)) {
            unhookMaps.get(member).unhook();
            unhookMaps.remove(member);
        }
    }

    /**
     * Sets the debug mode for the HookManager.
     *
     * @param bool If true, debug information (such as exceptions) will be printed.
     */
    public static void setDebug(boolean bool) {
        isDebug = bool;
    }

    /**
     * Stores information about the hooks applied to a method.
     */
    private static class MethodInfo {
        protected XC_AfterCallback after;
        protected XC_BeforeCallback before;
        protected XC_ReplaceCallback replace;

        /**
         * Sets the callback to be executed after the method is called.
         *
         * @param after The AfterCallback instance.
         * @return The updated MethodInfo object.
         */
        public MethodInfo setAfter(XC_AfterCallback after) {
            this.after = after;
            return this;
        }

        /**
         * Sets the callback to be executed before the method is called.
         *
         * @param before The BeforeCallback instance.
         * @return The updated MethodInfo object.
         */
        public MethodInfo setBefore(XC_BeforeCallback before) {
            this.before = before;
            return this;
        }

        /**
         * Sets the callback to replace the method implementation.
         *
         * @param replace The ReplaceCallback instance.
         * @return The updated MethodInfo object.
         */
        public MethodInfo setReplace(XC_ReplaceCallback replace) {
            this.replace = replace;
            return this;
        }
    }

    /**
     * Handles before and after hooks.
     */
    private static class HookCallback extends XC_MethodHook {
        private final MethodInfo info;

        /**
         * Initializes HookCallback with the corresponding method's information.
         *
         * @param member The method or constructor to hook.
         */
        public HookCallback(Member member) {
            this.info = methodMaps.get(member);
        }

        /**
         * Executes the after callback if defined.
         *
         * @param param The call frame context.
         */
        @Override
        public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
            try {
                super.afterHookedMethod(param);
                if (info != null && info.after != null) 
                      info.after.afterHook(param);
                
            } catch (Throwable err) {
                if (isDebug) err.printStackTrace();
            }
        }

        /**
         * Executes the before callback if defined.
         *
         * @param param The call frame context.
         */
        @Override
        public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
            try {
                super.beforeHookedMethod(param);
                
                if (info != null && info.before != null)
                      info.before.beforeHook(param);
                
            } catch (Throwable err) {
                if (isDebug) err.printStackTrace();
            }
        }
    }

    /**
     * Handles method replacement.
     */
    private static class ReplacementCallback extends XC_MethodReplacement {
        private final MethodInfo info;

        /**
         * Initializes ReplacementCallback with the corresponding method's information.
         *
         * @param member The method or constructor to replace.
         */
        public ReplacementCallback(Member member) {
            this.info = methodMaps.get(member);
        }

        /**
         * Replaces the method implementation if a replacement callback is defined,
         * otherwise invokes the original method.
         *
         * @param param The call frame context.
         * @return The result of the method call.
         * @throws Throwable if an error occurs during method execution.
         */
        @Override
        public Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            try {
                if (info != null && info.replace != null) 
                  return info.replace.replaceHook(param);
                
            } catch (Throwable err) {
                if (isDebug) err.printStackTrace();
            }
            return XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
        }
    }
}
