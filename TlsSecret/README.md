### Creating ssl keys for development

Fake certificates

```
openssl req -x509 -sha256 -nodes -days 365 -newkey rsa:2048 -keyout server.key -out server.crt  -config openssl.conf;
kubectl create -n istio-system secret tls gateway-tls-secret --key=server.key --cert=server.crt;
```

Trust the certificate by dragging it into keychain, opening it, expanding trust and select always trust.
