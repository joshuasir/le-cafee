# LeCafe: A Cafe Management Game

## Introduction

We will be exploring the implementation of various design patterns in the form of a command-line application game that simulates the management and operation of a restaurant. The game allows players to upgrade their restaurant by adding seats and hiring or upgrading cooks and waiters to serve customers. Each waiter, cook, and customer all operate concurrently, using their own threads to handle events and change states. The game is developed in Java and uses design patterns to ensure code reusability and sustainability. This case study presents how these design patterns are implemented in the game, showing the benefits and challenges of their use. Overall, this case study aims to provide insight into the role of design patterns in developing complex, interactive applications such as this restaurant management game.

## Methodology

First, we define the game flow by using an existing restaurant management game’s flow. We then adjust the features in the game, mainly simplifying the data representation. This process includes defining which features to keep and what states we want to have in the game. We then translate these features into classes that represent the data and their relationships. From there, we identify common design pattern problems present in the design and start development from that initial design. Designing the class diagram and coding then becomes an iterative process, where each iteration helps integrate or choose design patterns to create more maintainable and reusable code. After the final iteration, we conduct testing with our team members to ensure the game performs well and the design patterns are appropriate.

![Image](https://github.com/user-attachments/assets/37a6bad6-3d21-4dfe-bc9a-9d6616573e98)

## Class Diagram

[Click here for full image documentation](https://docs.google.com/document/d/1R6YROIVj3PGVPzGcuEQtHz0fWQS2AL6_mTxrZceK1os/edit?usp=sharing)

## Implementation

Le Cafe is a command-line interface program that uses threading to run in real time, providing updates to the user every second and allowing interaction by pausing while still running. In the pause menu, users can manage their budget to upgrade various aspects of the game, such as cafe capacity, waiter and chef skills, or hiring new staff. While the game is running, the CafeMediator class enables communication between objects in the program by adding them to a queue for processing. The queue is processed by changing the states of the objects at the speed of the waiter and chef skills. After the game ends, the program calculates the final score and writes it to a text file that stores all high scores.

## Design Pattern Formulation

### Singleton

The Singleton pattern is implemented in the `MainMenu` class, ensuring that only one instance of the restaurant exists at a time.

### State

Each model class in the program, including `Cook`, `Customer`, and `Waiter`, has its own set of states that are assigned one at a time. These states change periodically as the game runs to allow real-time score calculation.

### Factory

To obtain an instance of a model class, the program uses the Factory pattern through the `PersonFactory` class. This factory returns the desired class based on the type passed as a parameter, facilitated by each model extending the `Person` class.

### Mediator

The `CafeMediator` class helps the models in the program communicate with each other. If a sender model wants to interact with other models, the mediator adds the other models to the sender’s queue for processing.

### Observer

The Observer pattern is used in the `GeneratorManager` class, where there is a 25% chance every second that a customer will come to the cafe.

### Facade

The `CafeMenu` class serves as a facade for user interactions with the program. It centralizes user interactions and provides a simplified menu interface.

### Prototype

The `Mediator` class creates a new `ArrayList` and performs a deep copy of each element from the original collection using the `Cloneable` interface available in Java.

## Conclusion

This case study demonstrates the value of using design patterns such as the Mediator, Factory, and State patterns to enhance the reusability and sustainability of the codebase. These patterns provide well-tested mechanisms for delegation and composition, as well as other methods of reuse that do not rely on inheritance, allowing the code to be easily modified and adapted to changing requirements. The implementation of these patterns has contributed to efficient and flexible operations in the game, enabling maintainable concurrent execution of multiple restaurant staff and customers and the ability to upgrade and customize the restaurant. Overall, the careful implementation of design patterns significantly improves the maintainability and evolution of complex codebases.

## References

[Refactoring Guru: Design Patterns](https://refactoring.guru/design-patterns)
