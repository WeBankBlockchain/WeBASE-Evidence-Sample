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

    private static final String TRANS_CONTRACT_DEPLOY = "contract/deploy";
    private static final String TRANS_CALL = "trans/call";

    /**
     * setEvidence.
     * 
     * @param req parameter
     * @return
     */
    public BaseResponse setEvidence(ReqSetEvidence req) throws BaseException {
        BaseResponse baseRsp = new BaseResponse(ConstantCode.RET_SUCCEED);

        // sample StorageCell.sol
        String contractBin =
                "608060405234801561001057600080fd5b5060405161064b38038061064b83398101806040528101908080518201929190602001805182019291905050508160009080519060200190610053929190610072565b50806001908051906020019061006a929190610072565b505050610117565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100b357805160ff19168380011785556100e1565b828001600101855582156100e1579182015b828111156100e05782518255916020019190600101906100c5565b5b5090506100ee91906100f2565b5090565b61011491905b808211156101105760008160009055506001016100f8565b5090565b90565b610525806101266000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630d8e6e2c1461005c57806337a6beee146100ec578063788bc78c146101e8575b600080fd5b34801561006857600080fd5b50610071610251565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100b1578082015181840152602081019050610096565b50505050905090810190601f1680156100de5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156100f857600080fd5b506101016102f3565b604051808060200180602001838103835285818151815260200191508051906020019080838360005b8381101561014557808201518184015260208101905061012a565b50505050905090810190601f1680156101725780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156101ab578082015181840152602081019050610190565b50505050905090810190601f1680156101d85780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b3480156101f457600080fd5b5061024f600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061043a565b005b606060028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102e95780601f106102be576101008083540402835291602001916102e9565b820191906000526020600020905b8154815290600101906020018083116102cc57829003601f168201915b5050505050905090565b60608060006001818054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561038f5780601f106103645761010080835404028352916020019161038f565b820191906000526020600020905b81548152906001019060200180831161037257829003601f168201915b50505050509150808054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561042b5780601f106104005761010080835404028352916020019161042b565b820191906000526020600020905b81548152906001019060200180831161040e57829003601f168201915b50505050509050915091509091565b8060029080519060200190610450929190610454565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061049557805160ff19168380011785556104c3565b828001600101855582156104c3579182015b828111156104c25782518255916020019190600101906104a7565b5b5090506104d091906104d4565b5090565b6104f691905b808211156104f25760008160009055506001016104da565b5090565b905600a165627a7a723058201e2fc3acee78ec44951876256955cbc55a89d221447fe51e79ed52211633ba2f0029";
        String contractAbi = "[" + "{" + "\"name\":\"getStorageCell\"," + "\"constant\":true,"
                + "\"payable\":false," + "\"type\":\"function\"," + "\"stateMutability\":\"view\","
                + "\"inputs\":[],"
                + "\"outputs\":[{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"}]"
                + "}," + "{" + "\"name\":\"setVersion\"," + "\"constant\":false,"
                + "\"payable\":false," + "\"type\":\"function\","
                + "\"stateMutability\":\"nonpayable\","
                + "\"inputs\":[{\"name\":\"n\",\"type\":\"string\"}]," + "\"outputs\":[]" + "},"
                + "{" + "\"name\":\"getVersion\"," + "\"constant\":true," + "\"payable\":false,"
                + "\"type\":\"function\"," + "\"stateMutability\":\"view\"," + "\"inputs\":[],"
                + "\"outputs\":[{\"name\":\"\",\"type\":\"string\"}]" + "}," + "{"
                + "\"payable\":false," + "\"type\":\"constructor\","
                + "\"stateMutability\":\"nonpayable\","
                + "\"inputs\":[{\"name\":\"storageHash\",\"type\":\"string\"},{\"name\":\"storageInfo\",\"type\":\"string\"}]"
                + "}" + "]";
        List<Object> funcParam = new ArrayList<>();
        funcParam.add(req.getEvidenceHash());
        funcParam.add(req.getDesc());

        // set param
        DeployParam param = new DeployParam();
        param.setGroupId(req.getGroupId());
        param.setUuidDeploy(req.getUuid());
        param.setSignType(0);
        param.setContractBin(contractBin);
        param.setContractAbi(JSONArray.parseArray(contractAbi));
        param.setFuncParam(funcParam);

        // request transaction server
        baseRsp = requestTransServer(TRANS_CONTRACT_DEPLOY, RequestMethod.POST, param);
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
        BaseResponse baseRsp = new BaseResponse(ConstantCode.RET_SUCCEED);
        // set param
        TransCallParam param = new TransCallParam();
        param.setGroupId(groupId);
        param.setUuidDeploy(uuid);
        param.setFuncName("getStorageCell");
        // request transaction server
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
}
