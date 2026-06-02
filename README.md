# HealthCore

HealthCore manages a small network of hospitals, with 15 units currently under their administration. One of the main aspects of their network is its hierarchical structure. The network consists of hospitals; each divided into multiple departments. Each department supports a specific number of beds, staff, and equipment. 

---


# Development Environment

This project uses **Docker** and **VS Code Dev Containers** to ensure a consistent development environment across all team members.

## Prerequisites

Install the following tools:

### Docker Desktop

Download and install Docker Desktop:

https://www.docker.com/products/docker-desktop

Verify installation:

```bash
docker --version
docker compose version
```

### Visual Studio Code

Download:

https://code.visualstudio.com

### VS Code Extensions

Install:

* Dev Containers
* Docker

---

# Running the Project

## Build and Start Containers

From the project root:

```bash
docker compose up --build
```

The first build may take several minutes.

---

## Open the Project in the Dev Container

In VS Code:

1. Press `Ctrl + Shift + P`
2. Select:

```text
Dev Containers: Reopen in Container
```

VS Code will automatically connect to the development container.

---

## Start the Application

Inside the container terminal:


```bash
python run.py
```

depending on the project entry point.

The application should be available at:

```text
http://localhost:5000
```

---

# Development Workflow

## Pull Latest Changes

Before starting work:

```bash
git pull origin main
```

<!-- ---

## Create a Feature Branch

Never work directly on main.

```bash
git checkout -b feature/my-feature
```

Examples:

```bash
git checkout -b feature/login-page
git checkout -b feature/user-authentication
```

---

## Commit Changes

```bash
git add .
git commit -m "Add login page"
```

Push:

```bash
git push origin feature/login-page
```

Create a Pull Request on GitHub. -->

---

# Managing Python Dependencies


## Install a New Package

Inside the container:

```bash
pip install <package-name>
```

---

## Update requirements.txt

After installing new dependencies:

```bash
pip freeze > requirements.txt
```

Commit the updated requirements file:

```bash
git add requirements.txt
git commit -m "Add flask-login dependency"
```

---

# Docker Commands

### Start Containers

```bash
docker compose up
```

### Rebuild Containers

```bash
docker compose up --build
```

### Stop Containers

```bash
docker compose down
```

### View Logs

```bash
docker compose logs -f
```

---

# Troubleshooting

### Container Not Updating

Rebuild the container:

```bash
docker compose down
docker compose up --build
```

Then reopen in container:

```text
Ctrl + Shift + P
Dev Containers: Reopen in Container
```

---

### Missing Python Package

Verify installation:

```bash
pip list
```

If missing:

```bash
pip install <package-name>
pip freeze > requirements.txt
```

---