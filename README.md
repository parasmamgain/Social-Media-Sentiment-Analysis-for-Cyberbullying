# Social Media Sentiment Analysis for Cyberbullying

This repository contains the code for a machine learning model and a Java program that establishes a connection between a messaging queue and the NLP model. The machine learning model is used to detect abusive messages in the messaging queue. The Java program intercepts the message content and sends it to the NLP model for classification. If the NLP model classifies the message as abusive, the Java program sends a response back to the sender and does not further process the message. Otherwise, the Java program propagates the message to the channels and the queue of the queue manager.

## Benefits

This repository provides a number of benefits, including:

1. A complete solution for detecting abusive messages in a messaging queue.
2. A Java program that can be easily integrated with existing messaging systems.
3. A machine learning model that is trained on a large dataset of abusive and non-abusive messages.

## Usage

To use this repository, you will need to:

1. Clone the repository to your local machine.
2. Install the required dependencies.
3. Train the machine learning model (optional).
4. Run the Java program.

## Introduction


The paper discusses the problem of cyberbullying and proposes a solution using sentiment analysis to detect cyberbullying patterns and any aggressiveness over the social media platform. The proposed methodology would offer a secure and a reliable way to identify the real user or fake users behind a trolling account and identify the offensive classes under different categories like bully, aggressive, spam and normal users. The solution can also be integrated with various messaging services and can also be integrated with any other custom application depending on the need of the use.

## Problem Statement

Cyberbullying is a growing problem with no easy solutions. This paper proposes a new approach using sentiment analysis to detect cyberbullying patterns and aggressiveness on social media.

## Flow Diagram

<img width="522" alt="image" src="https://github.com/parasmamgain/mq-cyber-bullying/assets/11806853/2841277b-cf37-4355-8d86-fc692102db12">

## Implementation

Step 1: User connects to the messaging platform (IBM MQ in this case) using Java APIs.

Step 2: Once the connection is established, the user can send and receive messages via a particular queue in the queue manager.

Step 3: Whenever a message is sent, a Java module named "predictCategory" intercepts the message content and tries to determine the category of the message data using the ML model running in Colab notebook.

Step 4: If the ML model suggests that the message data is an abusive message, the same response is sent back to the sender and the message is not further processed (i.e., it is not sent to the queue manager and its queues).

Step 5: If the ML model suggests that the message is non-abusive, the message is further propagated to the channels and the queue of the queue manager and then is finally delivered to the recipient of the message.


## Summary

In summary, the paper proposes a scalable and feasible approach to detect cyberbullying using sentiment analysis. The proposed solution is secure and reliable, and can be integrated with various messaging services and custom applications.
