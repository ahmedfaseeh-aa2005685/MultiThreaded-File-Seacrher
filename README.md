# Parallel File Search Engine

## Project Description
The Parallel File Search Engine is a Java-based application designed to efficiently search for a specified keyword across multiple text files concurrently. It leverages advanced computing concepts such as multi-threading, socket programming, scheduling, and synchronization to achieve robust performance and scalability.

## Server Functionality
Upon running the server instance, the application performs the following tasks:
- Identifies the operating system to handle file paths and system-specific operations effectively.
- Determines the number of processor cores for optimized parallel processing.
- Creates threads based on the core count to distribute the workload evenly.
- Provides a user-friendly interface for choosing search distribution options: Equal Distribution or Round Robin Distribution.
- Indexes a specified directory containing text files for search operations.
- Reads file content, stores relevant information, and conducts case-insensitive keyword searches.
- Presents search results including files searched by each thread, keyword matches, and number of occurrences.
- Measures and reports total searching time and individual thread timing for performance evaluation.
- Implements multi-threading and synchronization to ensure data consistency and prevent race conditions.

## Client Interaction
The client interacts with the server by:
- Choosing a search distribution option and entering search parameters.
- Providing the search path and keyword to initiate the search process.
- Receiving and displaying search results on-screen before terminating the connection.

## Performance Measurements
The application measures and reports:
- Total searching time across all threads.
- Individual thread timing for load distribution analysis.
- Consistent and complete search results even with concurrent thread operations.

## Important Notes
1. Programming Language: Java
2. Avoid using Java's built-in file search libraries; utilize Linux commands for learning purposes.
3. Ensure proper multi-threading and synchronization implementation for data consistency and efficiency.

This project serves as a practical exercise to demonstrate understanding and implementation skills in advanced computing concepts within Java.
