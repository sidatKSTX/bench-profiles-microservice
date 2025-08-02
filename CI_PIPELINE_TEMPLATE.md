# CI/CD Pipeline Creation Prompt Template

Create a comprehensive GitHub Actions CI/CD pipeline for a Spring Boot microservice with the following requirements:

## Pipeline Requirements:
1. **Build & Test**: Maven build with tests
2. **SonarQube Analysis**: Code quality analysis with SonarCloud
3. **Security Scanning**: Trivy filesystem and container scans
4. **Docker Build**: Build and push to AWS ECR
5. **Container Security Audit**: Comprehensive vulnerability assessment

## Configuration Parameters:
- **Java Version**: 21
- **Service Directory**: `[SERVICE_NAME]`
- **ECR Repository**: `[ORGANIZATION]/[SERVICE_NAME]`
- **SonarQube Project Key**: `[GITHUB_ORG]_[REPO_NAME]`
- **SonarQube Organization**: Use `${{ secrets.SONAR_ORGANIZATION }}`  ✅ CRITICAL
- **AWS Region**: us-east-1

## Required GitHub Secrets:
- `SONAR_TOKEN` - SonarCloud authentication token
- `SONAR_ORGANIZATION` - SonarCloud organization name ✅ REQUIRED
- `AWS_ACCOUNT_ID` - AWS account identifier
- `AWS_ACCESS_KEY_ID` - AWS access credentials
- `AWS_SECRET_ACCESS_KEY` - AWS secret credentials

## SonarQube Configuration (CRITICAL):
```yaml
env:
  SONAR_PROJECT_KEY: "[GITHUB_ORG]_[REPO_NAME]"
  SONAR_ORGANIZATION: ${{ secrets.SONAR_ORGANIZATION }}  # ✅ MANDATORY

# Build command MUST include BOTH parameters:
run: mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=${{ env.SONAR_PROJECT_KEY }} -Dsonar.organization=${{ env.SONAR_ORGANIZATION }}
```

## Pipeline Structure:
1. **Build Job**: Tests, SonarQube analysis
2. **Build-and-Scan Job**: Docker build, Trivy scans, ECR push
3. **Container-Security-Audit Job**: Comprehensive security analysis
4. **Notification Job**: Pipeline completion summary

## Environment Variables Template:
```yaml
env:
  JAVA_VERSION: "21"
  MAVEN_OPTS: "-Xmx1024m"
  ECR_REGISTRY: ${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.us-east-1.amazonaws.com
  ECR_REPOSITORY: [ORGANIZATION]/[SERVICE_NAME]
  IMAGE_TAG: ${{ github.sha }}
  SERVICE_DIRECTORY: "[SERVICE_DIRECTORY]"
  SONAR_PROJECT_KEY: "[GITHUB_ORG]_[REPO_NAME]"
  SONAR_ORGANIZATION: ${{ secrets.SONAR_ORGANIZATION }}  # ✅ REQUIRED FOR SONARCLOUD
  AWS_REGION: "us-east-1"
```

## Triggers:
```yaml
on:
  push:
    branches:
      - main
  pull_request:
    types: [opened, synchronize, reopened]
  workflow_dispatch:
```

## Security Features:
- **Trivy Filesystem Scan**: Scan source code for vulnerabilities
- **Trivy Image Scan**: Scan Docker image for vulnerabilities
- **SARIF Upload**: Upload security results to GitHub Security tab
- **Security Gate**: Fail pipeline on CRITICAL vulnerabilities
- **Container Audit**: Comprehensive vulnerability analysis with thresholds

## Validation Checklist:
- [ ] Both `SONAR_PROJECT_KEY` and `SONAR_ORGANIZATION` are defined in env variables
- [ ] SonarQube command includes both `-Dsonar.projectKey` AND `-Dsonar.organization`
- [ ] All required secrets are referenced in the workflow
- [ ] Service directory and ECR repository names match project structure
- [ ] Security scans are configured with proper SARIF upload to GitHub Security
- [ ] Docker build context is set correctly
- [ ] AWS credentials and region are properly configured
- [ ] Pipeline includes proper error handling and notifications

## Common Pitfalls to Avoid:
1. **Missing SONAR_ORGANIZATION** - Always include both project key AND organization
2. **Incorrect working directory** - Ensure SERVICE_DIRECTORY matches actual folder structure  
3. **Missing secrets** - Verify all required secrets are configured in GitHub repo settings
4. **Hardcoded values** - Use environment variables for all configurable parameters
5. **Security scan failures** - Configure appropriate vulnerability thresholds

## Usage Instructions:
1. Replace `[SERVICE_NAME]`, `[GITHUB_ORG]`, `[REPO_NAME]` with actual values
2. Configure all required GitHub secrets
3. Create SonarCloud project with matching organization and project key
4. Set up AWS ECR repository
5. Test pipeline with a small commit to verify all components work

Use this template to create a zero-iteration, production-ready CI/CD pipeline that includes comprehensive testing, security scanning, and deployment capabilities.