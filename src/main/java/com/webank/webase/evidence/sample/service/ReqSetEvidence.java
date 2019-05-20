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

package com.webank.webase.evidence.sample.service;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import com.webank.webase.evidence.sample.base.ConstantCode;
import lombok.Data;

/**
 * SetEvidenceInfo.
 * 
 */
@Data
public class ReqSetEvidence {
    @NotNull(message = ConstantCode.GROUP_ID_IS_EMPTY)
    private int groupId;
    @NotBlank(message = ConstantCode.UUID_IS_EMPTY)
    private String uuid;
    private String contractAddress;
    private String version;
    @NotBlank(message = ConstantCode.HASH_IS_EMPTY)
    private String evidenceHash;
    private String desc;
}
