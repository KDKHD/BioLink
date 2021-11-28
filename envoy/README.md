### Creating ssl keys for development

```
openssl req -x509 -sha256 -nodes -days 3650 -newkey rsa:2048 -keyout tls-secret/server.key -out tls-secret/server.crt  -config openssl.conf
```

Trust the certificate by dragging it into keychain, opening it, expanding trust and select always trust.

### Creating envoy configmap
```
kubectl create configmap envoy-config --from-file=config/
```

### Creating envoy secret
```
kubectl create secret envoy-secret --from-file=secret/
```
