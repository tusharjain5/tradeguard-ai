# 🚀 TradeGuard AI - Intelligent Crypto Trading Agent

[![Java](https://img.shields.io/badge/Java-17-%23ED8B00.svg?logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-%236DB33F.svg?logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-%234479A1.svg?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-%23005C0F.svg?logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![Razorpay](https://img.shields.io/badge/Razorpay-Payment-%230C8B6D.svg?logo=razorpay&logoColor=white)](https://razorpay.com/)
[![Railway](https://img.shields.io/badge/Railway-Deployed-%230B0D0E.svg?logo=railway&logoColor=white)](https://railway.app/)

> **TradeGuard AI** is an adaptive BTC trading agent built for the **Bitget Hackathon 2026**. It combines real-time market analysis, AI-driven signals, and paper trading to deliver a complete trading experience.

---

## 📌 Table of Contents

- 🌟 Features
- 🏗️ Tech Stack
- 📊 Layer Architecture
- 🎯 How It Works
- 🚀 Live Demo
- 📦 Installation
- 🔧 Configuration
- 📁 Project Structure

---

## 🌟 Features

### 🔐 User System
- Registration & Login with email verification (OTP)
- Role-based access - FREE / PRO plans
- Secure password encryption with BCrypt
- Session management with Spring Security

### 💹 Paper Trading
- Virtual Balance - $1,000 (FREE) / $10,000 (PRO)
- BUY/SELL orders with real-time BTC price
- PnL Tracking - Real-time profit/loss calculation
- BTC Holdings - Total BTC in portfolio
- Trade History - Full audit trail

### 🧠 AI Strategy Engine
- EMA 9 & EMA 21 - Trend detection
- RSI 14 - Momentum analysis
- Signal Generation - BUY/SELL/HOLD with confidence scores
- Risk Management - LOW/MEDIUM/HIGH risk levels

### 📊 Dashboard & Analytics
- Live BTC Price from Bitget API
- TradingView Chart - Interactive price chart
- Market Analysis - AI-generated insights
- Top Gainers/Losers

### 💳 Payment Integration
- Razorpay Payment Gateway
- PRO Plan - ₹499/month
- Upgrade/Downgrade flow
- Secure payment verification

### 📱 Responsive Design
- Mobile-first approach
- Desktop, Tablet, Mobile - All devices supported
- Dark theme - Professional crypto trading UI

---

## 🏗️ Tech Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Core language |
| Spring Boot | 3.3.5 | Application framework |
| Spring Security | 6.x | Authentication & Authorization |
| Spring Data JPA | 3.x | Database ORM |
| Thymeleaf | 3.1 | Template engine |
| MySQL | 8.0 | Relational database |

### APIs & Integrations
| Service | Purpose |
|---------|---------|
| Bitget API | Live BTC price & candlestick data |
| TradingView | Interactive charts |
| Razorpay | Payment gateway |
| Gmail SMTP | Email verification |

---

### 📊 System Architecture

## Layer 1: User Interface (Frontend)
- Home Page
- Dashboard Page
- Strategy Page
- Paper Trading Page
- Trade History Page
- Profile Page

---

## Layer 2: Controller Layer (Request Handling)
- AuthController – Login, Register, Verify
- DashboardController – Dashboard & Paper Trading
- HomeController – Home Page
- MarketController – BTC Price API
- PaperTradingController – Buy, Sell, Close Trades
- PaymentController – Razorpay Payment

---

## Layer 3: Service Layer (Business Logic)
- UserService – Registration, verification, upgrade
- PaperTradingService – Buy/Sell trades, PnL calculation
- StrategyService – EMA/RSI signals, market analysis
- MarketDataService – Bitget API calls, live price
- PaymentService – Razorpay order creation, verification
- EmailService – OTP email sending

---

## Layer 4: Data Layer (JPA Entities)
- User Entity – id, email, password, role, balance, payment_done
- Trade Entity – id, user_id, signal, entry_price, exit_price, quantity, pnl, status

---

## Layer 5: Database
- MySQL Database
- Tables: users, trades

---

## Layer 6: External APIs
- Bitget API – Live BTC price, candlestick data
- TradingView – Interactive charts
- Razorpay – Payment gateway
- Gmail SMTP – Email verification


---

## 🎯 How It Works

### 🔄 Trading Flow

## 1. User registers (FREE/PRO)
## 2. Email verification (OTP)
## 3. Login to dashboard
## 4. View live BTC price & AI signals
## 5. Execute BUY/SELL trades (Paper Trading)
## 6. Track PnL & BTC holdings
## 7. Upgrade to PRO for unlimited trades

### 🧠 Strategy Logic

| Condition | Signal | Explanation |
|-----------|--------|-------------|
| EMA9 > EMA21 AND RSI < 70 | 🟢 BUY | Bullish trend, not overbought |
| EMA9 < EMA21 AND RSI > 30 | 🔴 SELL | Bearish trend, not oversold |
| Other conditions | 🟡 HOLD | Sideways market |

### 📊 Confidence & Risk

## Confidence Score = 60 + (70 - RSI) × 0.5
## Risk Level:
## LOW (RSI < 50)
## MEDIUM (50-70)
## HIGH (RSI > 70)

---

## 🚀 Live Demo

### 🔗 Live URL
[https://tradeguard-ai-production-d167.up.railway.app](https://tradeguard-ai-production-d167.up.railway.app)

## 📦 Installation

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Git

### Step 1: Clone Repository
```bash
git clone https://github.com/tusharjain5/tradeguard-ai.git
cd tradeguard-ai

## Step 2: Configure Database
CREATE DATABASE tradeguard_ai;

## Step 3: Update application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/tradeguard_ai
spring.datasource.username=root
spring.datasource.password=xxxxxxxxxx

## Step 4: Build & Run
mvn clean install
mvn spring-boot:run

## Step 5: Access Application
http://localhost:8080

### 🔧 Environment Variables
For production deployment, set these environment variables:

## Variable	Description
DB_URL	                       MySQL connection URL
DB_USERNAME	                   Database username
DB_PASSWORD	                   Database password
MAIL_USERNAME	                 Gmail username
MAIL_PASSWORD	                 Gmail app password
RAZORPAY_KEY_ID	               Razorpay API key ID
RAZORPAY_KEY_SECRET	           Razorpay API key secret

### 📁 Project Structure

tradeguard-ai/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── tradeguard/
│   │   │           ├── TradeguardAiApplication.java
│   │   │           ├── config/
│   │   │           │   ├── RazorpayConfig.java
│   │   │           │   └── SecurityConfig.java
│   │   │           ├── controller/
│   │   │           │   ├── AuthController.java
│   │   │           │   ├── DashboardController.java
│   │   │           │   ├── HomeController.java
│   │   │           │   ├── MarketController.java
│   │   │           │   ├── PaperTradingController.java
│   │   │           │   └── PaymentController.java
│   │   │           ├── dto/
│   │   │           │   ├── PaperTrade.java
│   │   │           │   ├── PaymentRequest.java
│   │   │           │   ├── RegisterRequest.java
│   │   │           │   ├── TradeHistory.java
│   │   │           │   ├── TradingDecision.java
│   │   │           │   └── VerifyRequest.java
│   │   │           ├── entity/
│   │   │           │   ├── Trade.java
│   │   │           │   └── User.java
│   │   │           ├── repository/
│   │   │           │   ├── TradeRepository.java
│   │   │           │   └── UserRepository.java
│   │   │           └── service/
│   │   │               ├── CustomUserDetailsService.java
│   │   │               ├── EmailService.java
│   │   │               ├── MarketDataService.java
│   │   │               ├── PaperTradingService.java
│   │   │               ├── PaymentService.java
│   │   │               ├── StrategyService.java
│   │   │               └── UserService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── fragments/
│   │           │   └── navbar.html
│   │           ├── dashboard.html
│   │           ├── home.html
│   │           ├── login.html
│   │           ├── paper-trading.html
│   │           ├── payment.html
│   │           ├── profile.html
│   │           ├── register.html
│   │           ├── strategy.html
│   │           ├── trade-history.html
│   │           └── verify.html
│   └── test/
├── pom.xml
├── README.md
└── .gitignore

🤝 Contributing
1.Fork the repository
2.Create feature branch (git checkout -b feature/AmazingFeature)
3.Commit changes (git commit -m 'Add AmazingFeature')
4.Push to branch (git push origin feature/AmazingFeature)
5.Open Pull Request

📄 License
This project is licensed under the MIT License.

🙏 Acknowledgments
1. Bitget - For API and hackathon opportunity
2. Razorpay - For payment gateway
3. TradingView - For charts
4. Spring Boot - For amazing framework

📞 Contact
Project Maintainer: Tushar Jain
GitHub: @tusharjain5

Built with ❤️ for Bitget Hackathon 2026

