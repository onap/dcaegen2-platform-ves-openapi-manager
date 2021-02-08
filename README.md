# VES OpenApi Manager

# Description
This application should partially validate incoming service distributions in SDC. It validates each artifact of type
VES_EVENT. Its purpose is to check whether schemaReferences of stndDefined events included in VES_EVENT artifacts are
matching the schemas which VES Collector contains.

# Instructions

## How to prepare environment for local ves-openapi-manager

1. Connect to lab and expose ports of message-router service by setting *spec.type* to NodePort.
   
2. Get exposed port of 3904 internal port of message-router.
   
   Set up connection configuration in *environment.config* file.
   - MESSAGE_ROUTER_PORT - exposed port of message-router.
   - RKE_IP - IP of lab RKE node.
   - WORKER_IP - IP of lab worker node.
   - SSH_LAB_KEY_PATH - path to lab SSH key.
    
3. Local ports forwarding is required to set up proper connection from local environment to message-router on the lab.
   
   Run this to enable port-forwarding (CTRL+C to end):
   ```
   make port-forwarding
   ```
## How to locally start ves-openapi-manager
Currently, there are two common ways to run application, both described below.

### Start in IntelliJ
Right click on Main class, then Run or Debug button.

### Start as Docker container
Run:
```
make all
```

### Lab connection verification
Correctly connected to lab application should print logs:
```
distribution client initialized successfuly
```
and
```
distribution client started successfuly
```

## How to use ves-openapi-manager
After preparing environment, starting ves-openapi-manager and successful connection to lab, application will listen for
service distributions taking place in SDC. 

When one takes place, it downloads each VES_EVENT artifact to validate its stndDefined events.

Results of validation are visible in two places:
- In SDC UI in Service -> Distributions view under specific distribution as component *ves-openapi-manager*.
  It might take few minutes to show results after service distribution.
  
- In logs of ves-openapi-manager, right after validation takes place.
  