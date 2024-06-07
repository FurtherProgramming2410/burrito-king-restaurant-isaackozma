Burrito King GUI Application 

GitHub repository link - https://github.com/FurtherProgramming2410/burrito-king-restaurant-isaackozma.git

Burrito King GUI application is a program that allows users to log in, register, place orders, pay for orders, collect/cancel orders, export order information manage their profile and much more. 

Program class design
The Burrito King GUI has fifteen classes within 4 packages. Each class serves as a valuable assest to make the program run correctly. Below I will list the classes and their purpose.

BurritoKingApp – This class serves as the “Main” class as it manages the flow between different screens. 
Important methods –
start(stage primaryStage) – Initializes the application
showLogin() – Displays log in 
showRegistration – Displays Registration 
showDashboard(User user) – Displays dashboard 
showProfile(User user) – Displays profile
showOrderOnDashBoard(User user) – Displays order payment
getDashBoard – Returns the dashboard instance
getOrderOnDashBoard()- returns the orderOnDashBoard instance. 

Dashboard – This class handles the main interface for users that are logged in, it give users the ability to add items to their order, collect/cancel orders, export orders, manage their order and profile, upgrade to vip. 
Important methods – 
createDashboard(User user) – Sets up the dashboard UI layout.
clearTempOrder() – clears the temporary order and basket items.
updateCollectingCredits(User user) – Updates the VIP credits display.
handleUpgradeToVIP(User user) – Handles the upgrade to VIP. 
handleCollectOrder(User user, TextField collectOrderIDField, TextField collectTimeField) – Handles order collection.
handleCancelOrder(User user, TextField cancelOrderIDField) – handles cancelled orders.

Login – Handles logging the user in.
Important methods – createLogin – sets up the log in UI layout and handles the login process.

OrderOnDashBoard – handles the order placement process.
Important methods – 
createOrderPlacemnt(User user) – sets up the order placement layout.

Profile – This class allows users to edit their profile
createProfile(User user) – sets up the UI for the class

Registration – Manages the registration process for the users 
Important methods – creatRegistration -sets up the UI and handles the registration process. 


PaymentInfo – This class validates the payment details 
Important method - 
validateCardNumber(String cardNumber) – Validates the card number
validateExpiryDate(String expiryDate) – Validates the expiry date
validateCVV(String cvv) – Validates the CVV

UserManager – This class handles user data, order management, ability to save data, and authentication. 
getInstance() – Returns the singleton instance of the UserManager.
registerUser(String username, String password, String firstName, String lastName)- Registers users.
loginUser(String username, String password) – Logs in user
updateUserProfile(String username, String firstName, String lastName, String password) – updates user profile. 
upgradeToVIP(String username, String email) – upgrades to VIP
placeOrder() – places order
collectOrder(String username, int orderID, LocalDateTime collectTime) – collects order and order pickup time.
exportOrder- exports order.
loadUsersFromFile() – loads user data from file
saveUsersToFile()- saves user data to file.
lodOrdersFromFile – Loads order data from file
saveOrdersToFile() – saves orders to file

FoodItem – Class for items the can be ordered 
Important methods – getPrice – returns the price of the item
getName- returns name of item

Meal – class for the meal and the discount applied 
Important methods  - meal() – applies discount for meal

Order – class for an order and its details
Important methods - addItem(KingItem item, int quantity) – Adds an item to the order
calculateTotal() – works out the total cost for the order
processPayment(double amountPaid, int creditsUsed) – Processes payment for the order. 
toString – Returns the string representation of the order. 

OrderTest- is my class for doing Junit test on the order class. 

User – This class is for the users data and orders.
getCurrentOrder()- Returns the current order
addOrder(Order order) – adds order to the list from a user.
useCredits(int credits) – Uses the amount of credits wanted by the user. 


 
Design pattern implemented 
The design patter that I implemented was a singleton pattern. UserManager is the class that implements the Singleton pattern. It ensures that there is only one instance of UserManager within the program. It is implemented to manage user data and orders across the program. 

Junit tests implemented 
The Junit test I implemented were to ensure the correctness of key functions. 

testAddItem()  - Tests adding items to an order
testCalculateTotal() – Tests the calculation of the total cost of the order.
testSetStatus() – Test the setting of the status of the order.
testSetOrderPlacedTime() – Test the setting of the order placed time
testToString() – test the string representation of the order. 
