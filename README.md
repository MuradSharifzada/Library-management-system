# 📚 Library Management System

Welcome to **Library Management System**, where managing books, authors, and students becomes... dare I say... *fun*? Okay, maybe "fun" is a stretch, but hey, at least we made it less of a headache! Built with **Java**, **Spring Boot**, and sprinkled with some developer magic, this system is here to rescue you from the chaos of misplaced books and forgotten return dates.

---

## 🛠 What Does This Thing Do?

Imagine you’re running a library (or just role-playing as a librarian for fun), and you’ve got:
- Books flying off the shelves (metaphorically, we hope).
- Students borrowing books like they’ve got infinite time to return them.
- Categories and authors multiplying faster than you can keep track.

This system handles it all, so you don’t have to! Here's what you get:

### 📖 Book Wizardry
- Search for books with pagination so you don't drown in overflowing book lists.
- Filter by categories because chaos is overrated.
- Add, edit, and delete books like a boss.

### 🏷 Category Control
- Add, update, and delete book categories. Yes, even that weird "Paranormal Romance" section.

### ✍️ Author Arsenal
- Keep a clean and organized list of authors. No more "Who wrote that book again?" moments.

### 🔄 Order & Return Magic
- Track orders and returns so you can give your brain a break.

### 🎓 Student Supervision
- Manage student profiles and borrowing history. Basically, you’re Big Brother, but for books.

### 🛡 Admin Powers
- A dashboard that gives admins full control. Think of it as your Batcave, but for librarians.

### 🔒 Fort Knox Security
- Session-based login using **Spring Security**. No unauthorized bookworms allowed.

### 📱 Sleek & Responsive
- A user interface so smooth, it’s practically butter. Built with **Thymeleaf** for a modern, responsive design.

---

## 🧰 Tech Stack (a.k.a. What We Used to Build This Beast)

- **Backend**: Java, Spring Boot (Spring MVC, Spring Security)
- **Frontend**: Thymeleaf (because we like pretty things)
- **Database**: PostgreSQL (because your data deserves the best)
- **Schema Management**: Liquibase (for when your DB decides to throw a tantrum)
- **Cloud Storage**: AWS S3 (for all your book cover image needs)
- **Containerization**: Docker (because portability matters)
- **CI/CD**: GitHub Actions (set it and forget it)
- **Hosting**: Render (because uptime is life)
- **Testing**: JUnit, Mockito (we don’t trust ourselves either)

---

## 🚀 How to Get This Party Started

### Prerequisites
Before you dive in, make sure you have:
- Java 17+ (the cooler, newer Java)
- Gradle (because we’re too hip for Maven)
- Docker (if you’re feeling fancy)
- PostgreSQL (local or cloud, we’re not picky)
- AWS S3 (for your image storage shenanigans)

### Steps to Bring the Magic to Life

1. **Clone the Repo**:
   ```bash
   git clone https://github.com/MuradSharifzada/Library-management-system.git
   cd Library-management-system
   ```

2. **Set Up Your Configurations**:
   - Crack open `application.yml` or `application.properties`.
   - Add your PostgreSQL credentials and AWS S3 bucket details. No credentials? No app. Sorry, them’s the rules.

3. **Build the Application**:
   ```bash
   ./gradlew build
   ```

4. **Run It Like You Mean It**:
   ```bash
   ./gradlew bootRun
   ```

5. **Open Your Browser**:
   ```
   http://localhost:8080
   ```

6. **Feeling Fancy? Run It in Docker**:
   1. Build the image:
      ```bash
      docker build -t library-management-system .
      ```
   2. Run the container:
      ```bash
      docker run -p 8080:8080 library-management-system
      ```
---

## 🔄 CI/CD That Just Works

Thanks to **GitHub Actions**, our pipeline is smarter than your average library card. It:
1. Runs automated tests (because bugs are so last season).
2. Builds a shiny new Docker image.
3. Deploys it directly to Render.

It’s like having your own DevOps team, but cheaper.

---

## 🛠 API Endpoints (For the Nerds)

### Public Endpoints
- **Get All Books**:
  ```
  GET /books
  ```
- **Get Book Details**:
  ```
  GET /books/{id}
  ```

### Admin Endpoints
- **Add a Book**:
  ```
  POST /admin/books
  ```
- **Update a Book**:
  ```
  PUT /admin/books/{id}
  ```
- **Delete a Book**:
  ```
  DELETE /admin/books/{id}
  ```
- **Add an Author**:
  ```
  POST /admin/authors
  ```
- **Add a Category**:
  ```
  POST /admin/categories
  ```

---

## 🧪 Testing (a.k.a. Breaking Stuff)

Don’t just take our word for it. Run the tests yourself:
```bash
./gradlew test
```

If everything passes, rejoice. If not, well… you know what to do.


We’re open to contributions! Got a feature idea or found a bug? Fork the repo, work your magic, and send us a pull request. Here’s how:
1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Your descriptive commit message"
   ```
4. Push your branch:
   ```bash
   git push origin feature-name
   ```
5. Open a pull request, and let’s make this project even better.
