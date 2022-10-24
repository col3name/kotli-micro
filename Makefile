buildProduct:
	cd product && ./gradlew clean build && cd ..

buildCustomer:
	cd customer && ./gradlew clean build && cd ..

buildOrder:
	cd order && ./gradlew clean build && cd ..

buildApiGateway:
	cd api-gateway && ./gradlew clean build && cd ..

up: buildProduct buildCustomer buildOrder buildApiGateway
	docker compose up -d

down:
	docker compose down

bench:
	ab -r -c 5 -n 600 "http://127.0.0.1:8000/api/v1/orders?client_id=2"