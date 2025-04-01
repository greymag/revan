# Revan

## Development

### Setup

Use `.dev/docker-compose.yml` to run the development environment (all, except the app itself).
Create `.env` file in `.dev` from `.env.template` in the root directory.

In IntelliJ IDEA, you should set the environment variables for the app in the run configuration.

See the `src/main/resources/application.yaml` file for the list of variables and 
see the `.env.template` file for the explanation of the variables.

Only unique variables is `MONGODB_CONNECTION_STRING`, it should be like `mongodb://user:password@host:port`.

## Deploy

### Setup deploy server

- Install Docker
```bash
sudo apt-get update
sudo apt-get install docker.io -y
```
- Enable and start Docker
```bash
sudo systemctl enable docker
sudo systemctl start docker
```
- Install Docker Compose
```bash
sudo apt-get install docker-compose -y
```
- Create a User for Revan
```bash
sudo adduser revan
```
- Add the user to the docker group
```bash
sudo usermod -aG docker revan
```
- Then:
   - Log in as `revan`, or 
   - Use `sudo -u revan %CMD%` when executing commands from `root` user.
- Create a directory for Revan app
```bash  
mkdir /home/revan/app
```
- Create a directory for the database
```bash
mkdir /home/revan/app/db
```
  - You need to set the correct permissions for the directory. User in `mongodb` group should have read and write permissions. 
  It should be `996`
    (check with `docker run --rm -it mongodb/mongodb-community-server:6.0-ubi8 id`)
  - add group for directory and give full access to it (you should do it as `root` user)
```bash
sudo chgrp -R 996 /home/revan/app/db
sudo chmod -R 770 /home/revan/app/db
```

All work will be done in `/home/revan/app` directory.

### Deploy Revan

On the push to the `main` branch, the GitHub Actions will build and push the Docker image to the GitHub Container Registry.

To deploy the app, follow the steps below:

- Login to deploy server as `revan` user
- Pull the Docker image
```bash
docker pull ghcr.io/greymag/revan:latest
```
- Place `docker-compose.yml` file in the `/home/revan/app` directory
```bash
wget https://raw.githubusercontent.com/greymag/revan/main/docker-compose.yaml
```
- Create `.env` (see `.env.template`) file and fill it.
- ```bash
wget https://raw.githubusercontent.com/greymag/revan/main/.env.template -O .env
nano .env
```
- Run the Docker container
```bash
docker-compose up -d
```

### Restart

```bash
docker-compose restart
````

Or you can stop and run it again:
```bash
docker-compose down
docker-compose up -d
```

### Update Revan

```bash
docker-compose pull
docker-compose up -d
```