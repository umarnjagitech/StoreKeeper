Store Keeper App

## Techstack

- Kotlin
- Jetpack Compose
- SQLite DB - Room

## Design

1. Authentication: email and pass
2. User operations
    1. Create Product
    2. Read Product
    3. Update Product
    4. Delete Product
3. Product Details
    1. Product Name
    2. Quantity
    3. Price
    4. Optional Product Image - Native Camera feature to capture 
	or upload product images
## Pages

- All pages are scrollable view columns.
- All data is stored locally

### 1. Auth Pages - Login and Signup

- **Sign Up** - Form and option to navigate to the **login** page
    - Name
    - Email
    - Confirm Email
    - Password
- **Login** - Form and option to navigate to the **Signup** page
    - Email
    - Password
- Credentials are stored locally on the Room DB.

### 2. Products Dashboard

- Either “No Product created, click ‘+’ to create one” or a List of cards of products containing
    - the name and quantity of each
    - View product button at the bottom of the card
- A floating action button icon to create a new product.

### 3. Create Product Page

- A form to update the following details
    - Product Name
    - Quantity
    - Price
    - Optional Image - Set a Default Product image where none is provided.
- Create Product button to save details.
- Redirects to the dashboard or product page after creation.

### 4. View Product Details Page

- Product Name
- Quantity
- Price
- Image - where one was uploaded, if none, then add a default “no image” or “product” graphic in place.
- Update Button - Edit or Pencil icon
- Delete Button - Trash Button

### 5. Update Product Page

- Option to update the product’s:
    - Name
    - Quantity
    - Price
    - Update or add a new image.
- Update button.

**Task Deliverables**

- Fully functional app with local database CRUD implementation
- Ability to add, view, edit, and delete products
- Native camera integration for adding product images
- Clean, responsive UI suitable for a store management app
- GitHub Repository (Public URL): Link to the repository containing your complete source code and README
- Demo Video Link: Upload a 2–4 minute app walkthrough and share a viewable link (Google Drive). The video should clearly demonstrate your app’s full flow, including adding, viewing, editing, and deleting products.
- README Instructions:
    - Include the release APK in your README.
    - Add all relevant links (GitHub repo, APK link, Demo video) inside the README.
    - Ensure your README is well-formatted, clear, and easy to read.

## Project Plan: Store Keeper App

**Phase 1: Project Setup and Core Data Model**

1.  **Project Setup:**
    *   Ensure the Android Studio project is correctly set up with Kotlin, Jetpack Compose, and necessary dependencies for Room and Navigation.
2.  **Room Database:**
    *   Define the `User` entity for authentication (Name, Email, Password).
    *   Define the `Product` entity with attributes: `id` (primary key), `name`, `quantity`, `price`, and `imageUri` (String to store the image path).
    *   Create `UserDao` and `ProductDao` with methods for all CRUD (Create, Read, Update, Delete) operations.
    *   Set up the `AppDatabase` class that extends `RoomDatabase`.

**Phase 2: Authentication**

1.  **UI - Login and Signup Screens (Jetpack Compose):**
    *   Create a "Signup" screen with fields for Name, Email, Confirm Email, and Password. Include navigation to the Login screen.
    *   Create a "Login" screen with fields for Email and Password. Include navigation to the Signup screen.
2.  **ViewModel:**
    *   Create an `AuthViewModel` to handle the business logic for user registration and login.
    *   Implement functions to interact with the `UserDao` to save and retrieve user data.
3.  **Navigation:**
    *   Implement navigation between the Login, Signup, and Products Dashboard screens. After a successful login or signup, the user should be directed to the dashboard.

**Phase 3: Product Management**

1.  **UI - Products Dashboard (Jetpack Compose):**
    *   Display a list of products in a `LazyColumn`. Each item will be a card showing the product name and quantity.
    *   Include a "View Product" button on each card.
    *   Show a message "No Product created, click ‘+’ to create one" if the product list is empty.
    *   Add a Floating Action Button (FAB) to navigate to the "Create Product" screen.
2.  **UI - Create Product Screen (Jetpack Compose):**
    *   Create a form with fields for Product Name, Quantity, and Price.
    *   Add a button to trigger the device's camera to take a picture or select one from the gallery.
    *   Display a preview of the selected image. If no image is selected, use a default placeholder.
    *   A "Create Product" button to save the product to the database.
3.  **UI - View Product Details Screen (Jetpack Compose):**
    *   Display all details of a selected product: Name, Quantity, Price, and the Image.
    *   Include "Update" (edit icon) and "Delete" (trash icon) buttons.
4.  **UI - Update Product Screen (Jetpack Compose):**
    *   Pre-fill a form with the existing product details.
    *   Allow the user to edit the Name, Quantity, Price, and Image.
    *   An "Update" button to save the changes.
5.  **ViewModel:**
    *   Create a `ProductViewModel` to manage the product data.
    *   Implement functions to interact with the `ProductDao` for all CRUD operations.
    *   Use LiveData or StateFlow to observe changes in the product list and update the UI accordingly.

**Phase 4: Camera and Image Handling**

1.  **Camera Integration:**
    *   Implement the logic to launch the device's camera and capture an image.
    *   Handle the result from the camera activity and get the image URI.
2.  **Image Storage:**
    *   Save the captured image to the app's internal or external storage.
    *   Store the image's URI or path in the `Product` entity in the Room database.
    *   Load and display images in the "View Product Details" and "Update Product" screens.

**Phase 5: Finalization and Packaging**

1.  **Testing:**
    *   Perform manual testing of all app features: authentication, product CRUD, and image handling.
2.  **UI Polish:**
    *   Ensure the UI is clean, responsive, and user-friendly.
    *   Check for consistent design and layout across all screens.
3.  **Build and Release:**
    *   Generate a signed APK for release.
4.  **Documentation:**
    *   Update the `README.md` file with:
        *   A link to the release APK.
        *   A link to the demo video.
        *   Clear instructions on how to build and run the app.
