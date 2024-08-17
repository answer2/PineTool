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

package dev.answer.test;

import android.os.Build;
import android.util.Log;
import dev.answer.pinetool.Pina;
import top.canyie.pine.PineConfig;

public class Main {
  private static final String TAG = "Main";

  public static void method_1() {
    Log.d(TAG, "I am method_1");
  }

  public static void method_2() {
    Log.d(TAG, "I am method_2");
  }

  public static void main() {
    try {
      PineConfig.debug = true; // 是否debug，true会输出较详细log
      PineConfig.debuggable = true; // 该应用是否可调试，建议和配置文件中的值保持一致，否则会出现问题

      // if you use ReplacementHook, Can't ues after and before
      Pina.after(
          Main.class.getDeclaredMethod("method_1"),
          callFrame -> {
            Log.d(TAG, "I am method_1 after");
          });

      Pina.before(
          Main.class.getDeclaredMethod("method_1"),
          callFrame -> {
            Log.d(TAG, "I am method_1 before");
          });

      Pina.replace(
          Main.class.getDeclaredMethod("method_2"),
          callFrame -> {
            Log.d(TAG, "I am method_2 replacement");
            return null;
          });

    } catch (Exception err) {
      err.printStackTrace();
    }
  }
}
