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

package dev.answer.pinetool.callback;

import de.robv.android.xposed.XC_MethodHook;

public interface XC_ReplaceCallback {
    Object replaceHook(XC_MethodHook.MethodHookParam param) throws Throwable;
}
