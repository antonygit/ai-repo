# Spring AI Chat Application

This is a Spring Boot application that provides REST APIs for text and image-based chat using OpenAI's GPT models through Spring AI.

## Features

- **Text Chat**: Send text questions and receive AI-generated responses
- **Image Chat**: Upload images with questions for visual AI analysis
- **Error Handling**: Proper error responses for invalid requests
- **Image Validation**: Supports JPEG, PNG, GIF, and WebP formats

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- OpenAI API Key

## Setup

1. Set your OpenAI API key as an environment variable:
   ```bash
   set OPENAI_API_KEY=your_openai_api_key_here
   ```

2. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on port 8082.

## API Endpoints

### Text Chat
**POST** `/api/chat/text`

Request body:
```json
{
  "question": "What is the capital of France?"
}
```

Response:
```
The capital of France is Paris.
```

### Image Chat
**POST** `/api/chat/image`

Content-Type: `application/json`

Request body:
```json
{
  "question": "What do you see in this image?",
  "imageUrl": "https://example.com/path/to/your/image.jpg"
}
```

Response:
```
[AI analysis of the image from the provided URL based on the question]
```

## Example Usage

### Using curl for text chat:
```bash
curl -X POST http://localhost:8082/api/chat/text \
  -H "Content-Type: application/json" \
  -d '{"question": "Explain quantum computing in simple terms"}'
```

### Using curl for image chat:
```bash
curl -X POST http://localhost:8082/api/chat/image \
  -H "Content-Type: application/json" \
  -d '{"question": "What do you see in this image?", "imageUrl": "https://example.com/path/to/your/image.jpg"}'
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── org/example/
│   │   │   └── Main.java                    # Spring Boot main class
│   │   └── com/example/demo/
│   │       ├── chat/
│   │       │   ├── controller/
│   │       │   │   └── ChatController.java  # REST endpoints
│   │       │   ├── dto/
│   │       │   │   ├── TextChatRequest.java # Text chat request DTO
│   │       │   │   └── ImageChatRequest.java# Image chat request DTO
│   │       │   └── util/
│   │       │       └── ImageUtils.java      # Image processing utilities
│   │       └── config/
│   │           └── ClientConfig.java        # HTTP client configuration
│   └── resources/
│       └── application.properties           # Application configuration
```

## Configuration

The application uses the following configuration in `application.properties`:

- `spring.ai.openai.api-key`: OpenAI API key (from environment variable)
- `spring.ai.openai.chat.options.model`: GPT model to use (default: gpt-4o)
- `spring.ai.openai.chat.options.temperature`: Response creativity (default: 0.7)
- `server.port`: Server port (default: 8082)

## Error Handling

The API provides appropriate error responses:
- `400 Bad Request`: Invalid input (missing image, unsupported format, etc.)
- `500 Internal Server Error`: Server-side errors with descriptive messages
