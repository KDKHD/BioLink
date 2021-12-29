Client : https://heezy.dev/
API: https://api.heezy.dev/

### Refresh istio certs
```
kubectl -n istio-system delete pod $(kubectl -n istio-system get pod -lapp=istio-ingressgateway -ojsonpath='{.items[0].metadata.name}')
```


### Creating certificates
Check TlsSecret README.md

