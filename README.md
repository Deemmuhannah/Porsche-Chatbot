PorscheChatbotGUI
=================

A modern, interactive Java Swing-based chatbot GUI for Porsche Jeddah dealership. The chatbot helps users inquire about Porsche car models, prices (before and after VAT), test drives, and buying options through a friendly conversational interface.

Features
--------

- Clean and modern GUI using Java Swing with styled chat messages
- Displays Porsche car models with details and pricing (including 15% VAT)
- Interactive conversational flow:
  - Lists available car models with convertible info
  - Provides detailed info and prices on selected models
  - Asks if the user wants to buy or schedule a test drive
  - Responds accordingly with confirmation messages
- Different text styles for user and bot messages for clarity
- Responsive input field with send button and enter key support
- Scrollable chat area with customized scrollbar styling

Requirements
------------

- Java Development Kit (JDK) 8 or above
- Compatible IDE or command line environment for running Java Swing applications

How to Run
----------

1. Clone or download this repository.
2. Open the project in your favorite Java IDE (e.g., IntelliJ IDEA, Eclipse) or use command line.
3. Compile the `PorscheChatbotGUI.java` file.
4. Run the main class:

   java PorscheChatbotGUI

Usage
-----

- Upon launching, the chatbot welcomes you to Porsche Jeddah.
- You can type messages in the input field and either press Enter or click the Send button.
- Ask "What car models do you have?" to see the full list of available Porsche models.
- Type a model name (e.g., "Porsche 911") to get detailed info and pricing.
- Respond with "buy" to simulate a purchase inquiry or "test drive" to schedule a test drive.
- You can ask about services, prices, or say goodbye anytime.

Code Structure
--------------

- `PorscheChatbotGUI` class:
  - Extends `JFrame` and implements the chatbot GUI and interaction logic.
  - Uses a `JTextPane` with styled documents to differentiate user and bot messages.
  - Maintains conversational state internally to guide dialogue flow.
  - Stores Porsche car info in a nested static class `CarInfo`.

Customization
-------------

- Modify or add Porsche models by editing the `cars` array.
- Update pricing, descriptions, or add more interaction states as needed.
- Style colors, fonts, and layouts are set in the constructor and can be customized.

