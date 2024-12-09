FROM golang:1.22.2 as builder

WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download

COPY . .
RUN go build -o user-service .

FROM alpine:latest

WORKDIR /root/
COPY --from=builder /app/user-service .
EXPOSE 8000
CMD ["./user-service"]
