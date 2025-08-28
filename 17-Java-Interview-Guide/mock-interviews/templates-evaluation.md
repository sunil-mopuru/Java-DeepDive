# Mock Interview Templates & Evaluation

## 🎯 **Mock Interview Structure (90 minutes)**

### **Phase 1: Warm-up & Background (15 minutes)**
**Interviewer Script**:
```
"Hi [Name], thanks for taking the time to interview with us today. 
I'm [Interviewer Name], [Title] here at [Company].

Let's start with you telling me about your background and 
what brought you to apply for this senior Java engineer position.

Follow-up questions:
- What's your experience with our tech stack?
- What kind of projects have you been working on recently?
- What attracts you to this role and our company?"
```

**Evaluation Criteria**:
- ✅ Clear communication of experience
- ✅ Relevant project examples  
- ✅ Alignment with role requirements
- ✅ Enthusiasm and preparation level

---

### **Phase 2: Technical Deep Dive (25 minutes)**
**Sample Questions by Seniority Level**:

#### **Senior Engineer (5-8 years)**
```java
// Question: Optimize this code for better performance
public List<Order> getOrdersByCustomer(String customerId) {
    List<Order> allOrders = orderRepository.findAll(); // N+1 problem!
    return allOrders.stream()
        .filter(order -> order.getCustomerId().equals(customerId))
        .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
        .collect(Collectors.toList());
}

// Expected improvements:
// 1. Proper repository method with query
// 2. Database-level sorting
// 3. Pagination for large result sets
// 4. Caching if appropriate
```

#### **Staff/Principal Engineer (8+ years)**
```java
// Question: Design a distributed cache invalidation system
/*
Requirements:
- 100+ application instances across multiple data centers
- Cache consistency within 5 seconds globally
- Handle network partitions gracefully
- Minimize infrastructure overhead

Design considerations:
- Event-driven architecture vs polling
- Consistency vs availability trade-offs  
- Failure scenarios and recovery
- Monitoring and observability
*/
```

**Evaluation Matrix**:
| Criteria | Excellent (4) | Good (3) | Adequate (2) | Poor (1) |
|----------|---------------|----------|--------------|----------|
| **Problem Understanding** | Asks clarifying questions, identifies edge cases | Understands core problem | Basic understanding | Misses key points |
| **Solution Approach** | Multiple solutions considered, trade-offs analyzed | Solid approach with reasoning | Working solution | Incomplete or flawed |
| **Code Quality** | Clean, efficient, well-structured | Good practices followed | Functional but basic | Poor structure/style |
| **Communication** | Explains thinking clearly, collaborative | Clear explanations | Adequate communication | Unclear or poor |

---

### **Phase 3: System Design (25 minutes)**
**Sample System Design Question**:
```
Design a URL shortener service (like bit.ly) that handles:
- 1 billion URLs shortened per month
- 100:1 read to write ratio  
- 99.9% availability requirement
- Analytics on click tracking
- Custom URLs for premium users

Follow-up deep dives:
- Database schema and sharding strategy
- Caching layers and cache invalidation
- Rate limiting and abuse prevention
- Global distribution and CDN strategy
- Monitoring and alerting setup
```

**Evaluation Framework**:

#### **Requirements Clarification (5 points)**
- ✅ Asks about scale (QPS, storage)
- ✅ Clarifies functional requirements
- ✅ Identifies non-functional requirements
- ✅ Discusses constraints and assumptions

#### **High-Level Design (10 points)**
- ✅ Clean architecture with proper separation
- ✅ Identifies major components
- ✅ Reasonable technology choices
- ✅ Considers scalability from start

#### **Detailed Design (10 points)**
- ✅ Database schema design
- ✅ API design and protocols
- ✅ Caching strategy
- ✅ Handles edge cases

#### **Scale & Performance (10 points)**
- ✅ Bottleneck identification
- ✅ Horizontal scaling approach
- ✅ Performance optimization strategies
- ✅ Monitoring and metrics

---

### **Phase 4: Behavioral Questions (20 minutes)**
**Leadership & Problem-Solving Scenarios**:

#### **Question 1: Technical Leadership**
```
"Tell me about a time when you had to lead a technically complex project 
with multiple stakeholders and tight deadlines. How did you approach it?"

Evaluation Points:
- Technical planning and architecture decisions
- Stakeholder management and communication
- Risk identification and mitigation
- Team coordination and delegation
- Results and lessons learned
```

#### **Question 2: Conflict Resolution**
```
"Describe a situation where you disagreed with a technical decision 
made by your team or manager. How did you handle it?"

Evaluation Points:
- Professional approach to disagreement
- Data-driven arguments
- Willingness to compromise
- Focus on business outcomes
- Relationship preservation
```

#### **Question 3: Mentoring & Growth**
```
"How do you approach helping junior developers grow their skills 
while maintaining delivery commitments?"

Evaluation Points:
- Structured approach to mentoring
- Balance of guidance and independence
- Knowledge sharing strategies
- Patience and empathy
- Measuring mentoring success
```

---

### **Phase 5: Questions for Interviewer (5 minutes)**
**Candidate Questions to Evaluate**:

**Strong Candidates Ask**:
- "What are the biggest technical challenges the team is facing?"
- "How does the engineering organization approach technical debt?"
- "What does the career growth path look like for this role?"
- "How do you measure engineering success and productivity?"

**Red Flags**:
- Only asks about compensation/benefits
- No questions prepared
- Generic questions that show no research
- Overly critical questions about company

---

## 📊 **Scoring & Decision Matrix**

### **Overall Scoring Framework**
```
Technical Skills (40%):
- Problem solving ability: /20
- Code quality and best practices: /20
- System design thinking: /20
- Technology depth: /20

Communication & Leadership (30%):
- Clear technical communication: /15
- Collaboration and teamwork: /15
- Leadership and mentoring: /15
- Stakeholder management: /15

Cultural Fit (20%):
- Alignment with company values: /10
- Learning mindset and curiosity: /10
- Adaptability and resilience: /10
- Passion for technology: /10

Experience Relevance (10%):
- Domain knowledge: /5
- Tech stack familiarity: /5
- Scale of previous projects: /5
- Leadership experience: /5

Total: /200 points
```

### **Decision Thresholds**:
- **Strong Hire (170-200)**: Exceeds expectations, clear value-add
- **Hire (140-169)**: Meets expectations with growth potential  
- **No Hire (120-139)**: Some concerns, not recommended
- **Strong No Hire (<120)**: Significant gaps, not suitable

---

## 🎭 **Role-Playing Scenarios**

### **Scenario 1: Production Crisis Simulation**
**Setup**: 
```
"It's 2 AM and you get paged about a critical production issue. 
The main application is responding slowly, and customer complaints are coming in. 
Walk me through your approach to diagnosing and resolving this."

Evaluation Points:
- Systematic problem-solving approach
- Proper escalation and communication
- Data gathering and hypothesis testing
- Incident management best practices
- Post-mortem and prevention mindset
```

### **Scenario 2: Technical Debt Discussion**
**Setup**:
```
"Your product manager wants to ship 5 new features next quarter, 
but your team believes you need to spend time addressing technical debt 
that's slowing down development. How do you handle this conversation?"

Evaluation Points:
- Business impact articulation
- Data-driven arguments
- Compromise and negotiation skills
- Stakeholder empathy
- Solution-oriented thinking
```

### **Scenario 3: Architecture Review**
**Setup**:
```
"A junior engineer has proposed an architecture for a new service 
that you think has some issues. How do you provide feedback 
while maintaining a positive team dynamic?"

Evaluation Points:
- Constructive feedback delivery
- Technical mentoring approach
- Collaborative problem-solving
- Respect for different perspectives  
- Focus on learning and growth
```

---

## 📋 **Interview Preparation Checklist**

### **Technical Preparation**
- ✅ Review core Java concepts (concurrency, JVM, collections)
- ✅ Practice system design problems (5-7 different domains)
- ✅ Code algorithm problems (focus on optimization)
- ✅ Prepare examples of past projects and technical decisions
- ✅ Study company's tech stack and architecture
- ✅ Review recent Java features and best practices

### **Behavioral Preparation**
- ✅ Prepare STAR stories for common scenarios
- ✅ Practice articulating technical concepts simply
- ✅ Prepare questions about team, culture, and challenges
- ✅ Research interviewer backgrounds (LinkedIn)
- ✅ Review company values and recent news
- ✅ Practice mock interviews with colleagues

### **Day-of Interview**
- ✅ Test technology setup (video, audio, screenshare)
- ✅ Have backup communication method ready
- ✅ Prepare quiet, well-lit interview space
- ✅ Have water and snacks available
- ✅ Arrive 10 minutes early
- ✅ Have questions and notepad ready