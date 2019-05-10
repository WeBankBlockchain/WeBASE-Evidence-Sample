/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.webank.webase.evidence.sample.base;

public interface ConstantCode {

    // return success
    RetCode RET_SUCCEED = RetCode.mark(0, "success");

    // paramaters check
    String GROUP_ID_IS_EMPTY = "{\"code\":203001,\"msg\":\"group id cannot be empty\"}";
    String UUID_IS_EMPTY = "{\"code\":203002,\"msg\":\"uuid cannot be empty\"}";
    String HASH_IS_EMPTY = "{\"code\":203003,\"msg\":\"evidence hash cannot be empty\"}";

    // system error
    RetCode SYSTEM_ERROR = RetCode.mark(103001, "system error");
    RetCode PARAM_VAILD_FAIL = RetCode.mark(103002, "param valid fail");
}
