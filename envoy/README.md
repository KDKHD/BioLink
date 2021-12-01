### Creating ssl keys for development

```
openssl req -x509 -sha256 -nodes -days 3650 -newkey rsa:2048 -keyout tls-secret/server.key -out tls-secret/server.crt  -config openssl.conf
```

Trust the certificate by dragging it into keychain, opening it, expanding trust and select always trust.

### Creating envoy configmap
```
kubectl delete configmap envoy-config
kubectl create configmap envoy-config --from-file=config/
```

### Creating tls secret
```
kubectl create secret tls tls-secret --cert=tls-secret/server.crt --key=tls-secret/server.key
```
