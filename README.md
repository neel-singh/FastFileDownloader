# 🚀 Fast File Downloader

A multithreaded file downloader built in Java using HTTP Range Requests to achieve faster and efficient downloads.

---

## 📌 Features

- ⚡ Multithreaded downloading
- 📦 Chunk-based file division
- 🌐 HTTP Range request support
- 🔄 Parallel data fetching
- 📊 Progress tracking using Atomic variables
- 🧩 File merging after download

---

## 🧠 How It Works

1. The program takes a file URL as input.
2. It fetches the file size from the server.
3. The file is divided into multiple byte ranges (chunks).
4. Each chunk is assigned to a separate thread.
5. Threads download their respective chunks using HTTP Range requests.
6. All chunks are saved as temporary files.
7. After all threads complete, chunks are merged into the final file.

---

## 🛠 Tech Stack

- Java
- Multithreading (Runnable, Thread)
- Networking (HttpURLConnection)
- Concurrency (AtomicLong)

---

## ▶️ How to Run

1. Clone the repository:

```bash
git clone https://github.com/<your-username>/FastFileDownloader.git
