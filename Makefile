# Makefile for playwright-interactions project

.PHONY: help clean build sources test package install check-updates

.DEFAULT_GOAL := help

# Help functionality
help: ## Display this help message
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z0-9_-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' ${MAKEFILE_LIST}

clean: ## Clean up the project (remove target folder)
	@echo "Cleaning up the project..."
	./mvnw clean

build: ## Build the project
	@echo "Building the project..."
	./mvnw compile

sources: ## Fetch sources and javadocs for dependencies
	@echo "Fetching sources and javadocs for dependencies..."
	./mvnw dependency:resolve-sources
	./mvnw dependency:resolve -Dclassifier=javadoc

test: ## Run tests
	@echo "Running tests..."
	./mvnw test verify

package: ## Package the project into a JAR
	@echo "Packaging the project..."
	./mvnw package

install: ## Install project locally
	@echo "Installing project..."
	./mvnw install

check-updates: ## Check for dependency updates (libraries, plugins, etc.)
	@echo "Checking for dependency updates..."
	./mvnw versions:display-dependency-updates
	./mvnw versions:display-plugin-updates
	./mvnw versions:display-property-updates