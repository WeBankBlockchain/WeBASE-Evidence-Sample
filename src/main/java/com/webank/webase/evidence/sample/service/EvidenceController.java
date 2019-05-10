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

import com.alibaba.fastjson.JSON;
import com.webank.webase.evidence.sample.base.BaseController;
import com.webank.webase.evidence.sample.base.BaseResponse;
import com.webank.webase.evidence.sample.base.exception.BaseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller.
 * 
 */
@Api(value = "/evidence", tags = "evidence interface")
@Slf4j
@RestController
@RequestMapping(value = "/evidence")
public class EvidenceController extends BaseController {
    @Autowired
    EvidenceService evidenceService;

    /**
     * setEvidence evidence.
     * 
     * @param req parameter
     * @param result checkResult
     * @return
     * @throws BaseException
     */
    @ApiOperation(value = "set evidence", notes = "set evidence")
    @ApiImplicitParam(name = "req", value = "evidence info", required = true,
            dataType = "ReqSetEvidence")
    @PostMapping("/set")
    public BaseResponse setEvidence(@Valid @RequestBody ReqSetEvidence req, BindingResult result)
            throws BaseException {
        log.info("setEvidence start. req:{}", JSON.toJSONString(req));
        checkParamResult(result);
        return evidenceService.setEvidence(req);
    }

    /**
     * getEvidence.
     * 
     * @param groupId groupId
     * @param uuid uuid
     * @return
     * @throws BaseException
     */
    @ApiOperation(value = "get evidence", notes = "get evidence")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "groupId", required = true,
                    dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "uuid", value = "uuid", required = true, dataType = "String",
                    paramType = "path")})
    @GetMapping("/get/{groupId}/{uuid}")
    public BaseResponse getAddress(@PathVariable("groupId") int groupId,
            @PathVariable("uuid") String uuid) throws BaseException {
        return evidenceService.getEvidence(groupId, uuid);
    }
}
