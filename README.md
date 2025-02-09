# Online Video Streaming Application

An online video streaming application built using **React Native**, **AWS S3**, **MySQL**, and **Spring Boot**.

## Features
- Upload videos to AWS S3
- Stream videos directly from AWS S3
- Secure API with Spring Boot and MySQL

## Tech Stack
- **Frontend**: React Native
- **Backend**: Spring Boot
- **Database**: MySQL
- **Storage**: AWS S3

## Installation
### Backend (Spring Boot)
1. Clone the repository:
   ```sh
   git clone https://github.com/ritik6559/OnStreamer-backend.git
   cd OnStreamer-backend
   ```
2. Configure **application.properties** with your MySQL and AWS credentials.
3. Run the application:
   ```sh
   mvn spring-boot:run


## Screenshots
![Home Screen](screenshots/home.png)
![Video Player](screenshots/player.png)
![Upload Screen](screenshots/upload.png)


## API Endpoints
| Method | Endpoint           | Description                              |
|--------|--------------------|------------------------------------------|
| POST   | `/api/v1/videos/upload`        | Upload a new video           |
| GET    | `/api/v1/listvideos`           | Get all videos               |
| GET    | `/api/v1/videos/stream/{id}`   | Stream a specific video      |

## Contributing
Feel free to contribute by creating a pull request or submitting an issue.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
For any inquiries, reach out to:
- **Your Name**
- **Your Email**
- **GitHub**: [ritik6559](https://github.com/ritik6559)
