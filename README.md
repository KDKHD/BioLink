Client : https://heezy.dev/
API: https://api.heezy.dev/

![alt text](https://github.com/KDKHD/BioLink/blob/master/kiali_graph.png?raw=true)
### Deploying Kubernetes Manifests
```
kubectl apply -f KubernetesManifests/
```

### Accessing kiali
```
kubectl port-forward svc/kiali -n istio-system 20001
```

### Accessing Minikube Kubernetes Dashboard
```
minikube dashboard
```


### Accessing gateway from Minikube
```
minikube tunnel
```

### Creating certificates
Check TlsSecret README.md

### Refresh istio certs
```
kubectl -n istio-system delete pod $(kubectl -n istio-system get pod -lapp=istio-ingressgateway -ojsonpath='{.items[0].metadata.name}')
```

### Use Telepresence

```
telepresence connect; 
telepresence list; // get interceptable client
telepresence intercept nextclientservice --port 3000:http --env-file ~/nextclientservice-intercept.env // select service and port to intercept
```

