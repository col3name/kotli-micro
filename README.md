### Architecture

![arch](docs/images/arch.jpg)

## Benchmark

### Benchmark api-gateway service

![order bench](docs/images/api-gateway-bench-20-300.png)

### Benchmark order service

![order bench](docs/images/order-bench-100-1000.png)

### Benchmark customer service

![order bench](docs/images/customer-bench-100-1000.png)

### Benchmark product service

![order bench](docs/images/product-bench-100-1000.png)

### Run

```make up```

### Stop

```make down```

### TODO

- [x] store to kafka request and response info
- [x] make benchmark
- [ ] write test
- [ ] setup all services on docker-compose.yml
- [ ] split application to layers
- [ ] move common code to common module
- [ ] service registry as library on common module for event validation
