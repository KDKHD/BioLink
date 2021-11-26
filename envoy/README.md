### Creating ssl keys for development

```
openssl req -x509 -sha256 -nodes -days 3650 -newkey rsa:2048 -keyout server.key -out server.crt  -config openssl.conf
```

Trust the certificate by dragging it into keychain, opening it, expanding trust and select always trust.
