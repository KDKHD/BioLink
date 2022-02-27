

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
telepresence intercept [SERVICE NAME] --port 3000:http --env-file ~/intercept.env // select service and port to intercept
# Copy content of ~/intercept.env and paste it into gradle->edit configuration->environment variables->paste
# Run service locally and traffic will be sent to the local service
telepresence quit
```

