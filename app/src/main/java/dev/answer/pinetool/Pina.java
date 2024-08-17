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
import dev.answer.pinetool.callback.AfterCallback;
import dev.answer.pinetool.callback.BeforeCallback;
import dev.answer.pinetool.callback.ReplaceCallback;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;
import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;
import top.canyie.pine.callback.MethodReplacement;

public class Pina {
    private static final String TAG = "Pina";

    // Maps to store method information, hooks, and replacements
    private static final Map<Member, MethodInfo> methodMaps = new HashMap<>();
    private static final Map<Member, HookCallback> hookMaps = new HashMap<>();
    private static final Map<Member, ReplacementCallback> replaceMaps = new HashMap<>();

    /**
     * Adds an after hook to the specified method.
     *
     * @param member   The method to hook.
     * @param callback The callback to execute after the method is called.
     */
    public static void after(Member member, AfterCallback callback) {
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
                Pine.hook(member, hook);
            }
        }
    }

    /**
     * Adds a before hook to the specified method.
     *
     * @param member   The method to hook.
     * @param callback The callback to execute before the method is called.
     */
    public static void before(Member member, BeforeCallback callback) {
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
                Pine.hook(member, hook);
            }
        }
    }

    /**
     * Replaces the specified method with the provided callback.
     *
     * @param member   The method to replace.
     * @param callback The callback that replaces the method.
     */
    public static void replace(Member member, ReplaceCallback callback) {
        if (member == null) {
            Log.e(TAG, "member can't be null");
            return;
        }

        MethodInfo info = new MethodInfo().setReplace(callback);
        methodMaps.put(member, info);
        if (!replaceMaps.containsKey(member)) {
            ReplacementCallback replace = new ReplacementCallback(member);
            replaceMaps.put(member, replace);
            Pine.hook(member, replace);
        }
    }

    /**
     * Stores information about the hooks applied to a method.
     */
    private static class MethodInfo {
        protected AfterCallback after;
        protected BeforeCallback before;
        protected ReplaceCallback replace;

        public MethodInfo setAfter(AfterCallback after) {
            this.after = after;
            return this;
        }

        public MethodInfo setBefore(BeforeCallback before) {
            this.before = before;
            return this;
        }

        public MethodInfo setReplace(ReplaceCallback replace) {
            this.replace = replace;
            return this;
        }
    }

    /**
     * Handles before and after hooks.
     */
    private static class HookCallback extends MethodHook {
        private final MethodInfo info;

        public HookCallback(Member member) {
            this.info = methodMaps.get(member);
        }

        @Override
        public void afterCall(Pine.CallFrame callFrame) throws Throwable {
            if (info != null && info.after != null) {
                info.after.afterHook(callFrame);
            }
        }

        @Override
        public void beforeCall(Pine.CallFrame callFrame) throws Throwable {
            if (info != null && info.before != null) {
                info.before.beforeHook(callFrame);
            }
        }
    }

    /**
     * Handles method replacement.
     */
    private static class ReplacementCallback extends MethodReplacement {
        private final MethodInfo info;

        public ReplacementCallback(Member member) {
            this.info = methodMaps.get(member);
        }

        @Override
        public Object replaceCall(Pine.CallFrame callFrame) throws Throwable {
            if (info != null && info.replace != null) {
                return info.replace.replaceHook(callFrame);
            }
            return callFrame.invokeOriginalMethod();
        }
    }
}
