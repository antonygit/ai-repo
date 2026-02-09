# Test Commands for Spring AI Chat Application

## Prerequisites
Make sure your application is running on port 8080

## Health Check
GET http://localhost:8080/api/chat/health

## Text Chat Test
POST http://localhost:8080/api/chat/text
Content-Type: application/json

{
  "question": "Hello, how are you today?"
}

## Image Chat Test
POST http://localhost:8080/api/chat/image
Content-Type: application/json

{
  "question": "What do you see in this image?",
  "imageUrl": "https://example.com/path/to/your/image.jpg"
}

## Test with curl (PowerShell)
```powershell
# Health check
Invoke-RestMethod -Uri "http://localhost:8080/api/chat/health" -Method GET

# Text chat
Invoke-RestMethod -Uri "http://localhost:8080/api/chat/text" -Method POST -ContentType "application/json" -Body '{"question": "Hello, how are you?"}'

# Image chat with URL
Invoke-RestMethod -Uri "http://localhost:8080/api/chat/image" -Method POST -ContentType "application/json" -Body '{"question": "What do you see in this image?", "imageUrl": "https://example.com/image.jpg"}'
```

## Postman Configuration

### Text Chat
1. Method: POST
2. URL: http://localhost:8080/api/chat/text
3. Headers: 
   - Content-Type: application/json
4. Body (raw JSON):
   ```json
   {
     "question": "What is artificial intelligence?"
   }
   ```

### Image Chat
1. Method: POST
2. URL: http://localhost:8080/api/chat/image
3. Headers: 
   - Content-Type: application/json
4. Body (raw JSON):
   ```json
   {
     "question": "What do you see in this image?",
     "imageUrl": "https://example.com/path/to/your/image.jpg"
   }
   ```

## Expected Responses

### If API key is not configured:
Status: 503 Service Unavailable
Body: "OpenAI API key is not configured. Please set the OPENAI_API_KEY environment variable with your API key from https://platform.openai.com/account/api-keys"

### If API key is invalid:
Status: 401 Unauthorized  
Body: "Invalid OpenAI API key. Please check your API key at https://platform.openai.com/account/api-keys"

### If successful:
Status: 200 OK
Body: [AI response to your question]
