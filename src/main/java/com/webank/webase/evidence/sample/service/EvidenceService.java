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

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.webank.webase.evidence.sample.base.BaseResponse;
import com.webank.webase.evidence.sample.base.ConstantCode;
import com.webank.webase.evidence.sample.base.ConstantProperties;
import com.webank.webase.evidence.sample.base.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * SignService.
 * 
 */
@Slf4j
@Service
public class EvidenceService {
    @Autowired
    ConstantProperties properties;
    @Autowired
    RestTemplate restTemplate;

    private static final String TRANS_SEND = "trans/send";
    private static final String TRANS_CALL = "trans/call";
    private static final String TRANS_EVENT = "trans/event/%s/%s";

    /**
     * setEvidence.
     * 
     * @param req parameter
     * @return
     */
    public BaseResponse setEvidence(ReqSetEvidence req) throws BaseException {
        BaseResponse baseRsp = new BaseResponse(ConstantCode.RET_SUCCEED);

        // StorageFactory.sol abi
        String contractAbi = "[" + "{" + "\"name\":\"newStorage\"," + "\"constant\":false,"
                + "\"payable\":false," + "\"type\":\"function\","
                + "\"stateMutability\":\"nonpayable\","
                + "\"inputs\":[{\"name\":\"version\",\"type\":\"string\"},"
                + "{\"name\":\"storageHash\",\"type\":\"string\"},"
                + "{\"name\":\"storageInfo\",\"type\":\"string\"}],"
                + "\"outputs\":[{\"name\":\"\",\"type\":\"address\"}]" + "}," + "{"
                + "\"name\":\"newStorageEvent\"," + "\"type\":\"event\","
                + "\"anonymous\":\"false\","
                + "\"inputs\":[{\"indexed\":\"false\",\"name\":\"addr\",\"type\":\"address\"}]"
                + "}" + "]";
        // evidence info
        List<Object> funcParam = new ArrayList<>();
        funcParam.add(req.getVersion());
        funcParam.add(req.getEvidenceHash());
        funcParam.add(req.getDesc());

        // new evidence
        TransSendParam param = new TransSendParam();
        param.setGroupId(req.getGroupId());
        param.setUuidStateless(req.getUuid());
        param.setSignType(0);
        param.setContractAbi(JSONArray.parseArray(contractAbi));
        param.setContractAddress(req.getContractAddress());
        param.setFuncName("newStorage");
        param.setFuncParam(funcParam);

        // request transaction server
        baseRsp = requestTransServer(TRANS_SEND, RequestMethod.POST, param);
        return baseRsp;
    }

    /**
     * getEvidence.
     * 
     * @param groupId
     * @param uuid
     * @return
     */
    public BaseResponse getEvidence(int groupId, String uuid) throws BaseException {
        // get contractAddress
        BaseResponse response = new BaseResponse();
        response = requestTransServer(String.format(TRANS_EVENT, groupId, uuid), RequestMethod.GET,
                null);
        NewStorageEvent event = object2JavaBean(response.getData(), NewStorageEvent.class);
        String contractAddress = (String) event.getNewStorageEvent().get(0);

        // StorageCell.sol abi
        String contractAbi = "[" + "{" + "\"name\":\"getStorageCell\"," + "\"constant\":true,"
                + "\"payable\":false," + "\"type\":\"function\"," + "\"stateMutability\":\"view\","
                + "\"inputs\":[],"
                + "\"outputs\":[{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"},"
                + "{\"name\":\"\",\"type\":\"string\"}]"
                + "},{"
                + "\"payable\":false," + "\"type\":\"constructor\","
                + "\"stateMutability\":\"nonpayable\","
                + "\"inputs\":[{\"name\":\"version\",\"type\":\"string\"},"
                + "{\"name\":\"storageHash\",\"type\":\"string\"},"
                + "{\"name\":\"storageInfo\",\"type\":\"string\"}]"
                + "}" + "]";
        // set param
        TransCallParam param = new TransCallParam();
        param.setGroupId(groupId);
        param.setContractAbi(JSONArray.parseArray(contractAbi));
        param.setContractAddress(contractAddress);
        param.setFuncName("getStorageCell");

        // request transaction server
        BaseResponse baseRsp = new BaseResponse(ConstantCode.RET_SUCCEED);
        baseRsp = requestTransServer(TRANS_CALL, RequestMethod.POST, param);
        return baseRsp;
    }

    /**
     * requestTransServer.
     * 
     * @param uri
     * @param httpType
     * @param params
     * @return
     * @throws BaseException
     */
    private BaseResponse requestTransServer(String uri, RequestMethod httpType, Object params)
            throws BaseException {
        log.info("start requestTransServer. uri:{} httpType:{} params:{}", uri, httpType,
                JSON.toJSONString(params));
        BaseResponse response = new BaseResponse(ConstantCode.RET_SUCCEED);

        String url = String.format(properties.getTransBaseUrl(), uri);
        log.info("request url: {}", url);
        try {
            if (httpType == null) {
                log.info("httpType is empty.use default:get");
                httpType = RequestMethod.GET;
            }
            // get
            if (httpType.equals(RequestMethod.GET)) {
                response = restTemplate.getForObject(url, BaseResponse.class);
            }
            // post
            if (httpType.equals(RequestMethod.POST)) {
                response = restTemplate.postForObject(url, params, BaseResponse.class);
            }
        } catch (RuntimeException ex) {
            log.warn("fail requestTransServer", ex);
            throw new BaseException(ConstantCode.SYSTEM_ERROR);
        }

        log.info("end requestTransServer. response:{}", JSON.toJSONString(response));
        return response;
    }

    /**
     * conver object to java bean.
     */
    public static <T> T object2JavaBean(Object obj, Class<T> clazz) {
        if (obj == null || clazz == null) {
            log.warn("object2JavaBean. obj or clazz null");
            return null;
        }
        String jsonStr = JSON.toJSONString(obj);
        return JSON.parseObject(jsonStr, clazz);
    }
}
