# Runops JDB

## Install step by step

### 1. Generating or Downloading JAR file

### 2. Runops open session

The runops jdbc driver, needs the _authentication bearer_ with a valid _JWT_ in order to make authorized requests to the Runops API.
You can do this either by passing the JWT through system properties or by file `~/.runops/config`.
The following code will get a valid JWT and create the needed file to the driver.

```shell
cp .env.dev .env
```
Set your email
```shell
EMAIL=myemail@ebanx.com
```
./runops_login.sh
```

The output will be something like:
```shell
Trying to open your browser now. If it fails, use the link below to continue:
https://runops.us.auth0.com/authorize?...
Paylaod received:{"sub":"google-oauth2|...","aud":["...","..."],"https://runops.io/email":"...","azp":"...","scope":"openid profile email","iss":"...","exp":1657244359,"iat":1657157959}
Expires in:2022-07-07T22:39:19
```


### 3. IDE Configuration
<ul>
  <li>1. Data Sources and Drivers</li>
    <ul>
      <li>1.1. In left panel side </li>
        <ul>
          <li>1.1.1. click in `Drivers` tab</li>
          <li>1.1.2. click in icon `+`</li>
        </ul>
      <li>1.2. In right panel side</li>
        <ul>
          <li>1.2.1. In Driver File click in icon `+` and choose file jar downaloed ou generated before</li>
          <li>1.2.2. In Name set `Postgres Runops`</li>
          <li>1.2.3. In General->Class, choose: `ninja.ebanx.runops.Driver`</li>
          <li>1.2.4. In Options->Others [Icon, Dialect] choose: `PostgresSQL`</li>
          <li>1.2.5. In Advanced->VM home path choose: `...../Library/Java/JavaVirtualMachines/openjdk-18.0.1.1/Contents/Home` </li>
          <li>1.2.6. Click in `Applay` button</li>
        </ul>
     </ul>
  <li>2. click in `Data Sources` tab</li>
      <ul>
        <li>2.1. click in icon `+`</li>
        <li>2.2. You have to choose `User Driver->Postgres Runops` </li>
        <li>2.3. In General->Authentication choose: `no auth`</li>
        <li>2.4. In General->URL set: `jdbc:runops://read-akkad-production/akkad`</li>
        <li>2.5. Click in `Ok` button</li>
      </ul>
</ul>

![Step1](docs/image1.png "Step2")
![Step2](docs/image2.png "Step2")
![Step3](docs/image3.png "Step3")
![Step4](docs/image4.png "Step4")
![Step5](docs/image5.png "Step5")
