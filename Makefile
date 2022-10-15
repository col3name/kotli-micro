buildProduct:
	cd product && ./gradlew clean build && cd ..
buildCustomer:
	cd customer && ./gradlew clean build && cd ..

up: buildProduct buildCustomer
	docker compose up -d

down:
	docker compose down