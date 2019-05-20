# 存证案例说明文档

[TOC]

## 1、环境

软件：jdk1.8.0_121或以上

提前部署相关服务（管理台、交易服务子系统等）。

## 2、合约信息

本实例以简单的存证案例进行说明，主要存储数据hash和说明。

**在管理台导入以下两个合约，然后部署StorageFactory.sol，得到合约地址**

存证合约StorageCell.sol：

```
pragma solidity ^0.4.25;

contract StorageCell{

    string _version;
    string _storageHash;
    string _storageInfo;

    constructor (string version, string storageHash, string storageInfo) public {
       _version = version;
       _storageHash = storageHash;
       _storageInfo = storageInfo;
    }

    function getStorageCell() public constant returns(string, string, string){
        return(_version,_storageHash,_storageInfo);
    }
}
```

工厂合约StorageFactory.sol：

```
pragma solidity ^0.4.25;
import "./StorageCell.sol";

contract StorageFactory{

    event newStorageEvent(address addr);

    function newStorage(string version, string storageHash, string storageInfo)public returns(address) {
        StorageCell cell = new StorageCell(version, storageHash, storageInfo);
        newStorageEvent(cell);
        return cell;
    }
}
```

## 3、存证和取证说明

### 3.1 存证

​    工程EvidenceController类下的/evidence/set接口。每次存证会调用交易服务子系统的交易请求接口，需传入群组编号、交易业务流水号、签名类型、合约abi（工厂合约StorageFactory的abi）、合约地址（步骤2中部署工厂合约StorageFactory的合约地址）、方法名（StorageFactory合约的“newStorage”方法）、参数（版本、数据hash和说明）。交易服务子系统会通过轮询服务发送上链。

### 3.2 取证

​    工程EvidenceController类下的/evidence/get接口。每次取证会调用交易服务子系统的交易查询接口，需传入群组编号、合约abi（存证合约StorageCell的abi，可通过管理台编译获得）、合约地址（通过3.1的群组编号和交易业务流水号查询工厂合约StorageFactory的event信息获得）、方法名（存证合约StorageCell的“getStorageCell”方法）。实时请求交易服务子系统，返回存证相关信息（版本、数据hash和说明）。

## 4、部署调用示例

​    本工程使用了swagger框架，以下通过swagger api进行调用说明。

### 4.1 修改配置

​    修改本工程resources下application.yml文件，看端口和交易服务子系统URL是否需要修改：

```
server: 
  # 本工程端口
  port: 8086
  context-path: /webase-evidence-sample

constant: 
  # 交易服务子系统URL
  transBaseUrl: http://localhost:8082/webase-transaction/%s
  
logging: 
  config: classpath:log4j2.xml
```

### 4.2 启动

​    启动本工程的Application类。

### 4.3 访问

​    服务启动后，调用swagger链接，服务ip和端口根据实际修改

​    [http://localhost:8086/webase-evidence-sample/swagger-ui.html](http://localhost:8082/webase-transaction/swagger-ui.html)

![](./img/home.png)

### 4.4 存证

​    输入工厂合约地址（步骤2），存证说明，存证hash，群组编号，交易业务流水号，存证版本：

![](./img/set1.png)

​    执行：

![](./img/set2.png)

​    执行结果（code为0代表正常）：

![](./img/set3.png)

### 4.5 取证

​    输入群组编号和业务流水后执行：

![](./img/get1.png)

​    执行结果（code为0代表正常）：

![](./img/get2.png)
