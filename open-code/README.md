<img src="../docs/images/playground_banner.png" alt="banner">

# OpenBot Playground

OpenBot Playground is a drag and drop platform to support OpenBot application, where anyone can build instructions for the robot.

## Getting Started

You can run this application directly from the [Link](http://openbot.itinker.io "Link").

or you can run it locally via creating a local copy of the project.
To achieve this, navigate into the `openBot/open-code` folder, set environment variables and run application.

## Environment Variables

create a `.env` file inside `open-code` folder.
To run this project, you will need to add the following environment variables to your .env file.

```
REACT_APP_FIREBASE_API_KEY=<REACT_APP_FIREBASE_API_KEY>
REACT_APP_AUTH_DOMAIN=<REACT_APP_AUTH_DOMAIN>
REACT_APP_PROJECT_ID=<REACT_APP_PROJECT_ID>
REACT_APP_STORAGE_BUCKET=<REACT_APP_STORAGE_BUCKET>
REACT_APP_MESSAGING_SENDER_ID=<REACT_APP_MESSAGING_SENDER_ID>
REACT_APP_APP_ID=<REACT_APP_APP_ID>
REACT_APP_MEASUREMENT_ID=<REACT_APP_MEASUREMENT_ID>
GENERATE_SOURCEMAP=false
```

Install

If you're using npm

```bash
npm install
```

In case of yarn

```bash
yarn install
```

Run application

```bash
npm start
```

## Key features

### Application Features

1. Sync your Google Drive with the OpenBot, and it will automatically save the data on it.
2. Store the data into local storage.
3. Scan the output QR directly from the OpenBot application to run the program.
4. In OpenBot mobile apps, after logging in, you can retrieve saved files from Google Drive and load them with just one
   click.
5. Design the instructions for OpenBot with zero code.
6. Fully responsive design optimized for mobile and tablet browsing.

## OpenBot Playground Screens

### Home Page

The `OpenBot Playground` starts with homepage that contains following component:

- [Header](#header) : contains change theme and signIn user section
- [Carousel](#Carousal): explain how the Playground works
- [Project Section](#project-section) : List of Projects and Create new Project button

### PlayGround Page

The `PlayGround` page is a key feature of the `OpenBot Playground` platform that provides a variety of coding blocks for
users to create different types of functionality, such as Control, Loops, Movement, Operators, Sensors, and more.

- [Header](#header) : contains project name, help centre, change theme and signIn user section.
- [Workspace](#workSpace): Space where users can drag and drop the coding blocks to generate their code, which can be
  converted into both JavaScript and Python.
- [Playground Bottom Bar](#Playground Bottom Bar) : contains generate code ,upload in drive, zoom in and out , undo,
  redo buttons.

### Header

The header of the `home ` screen has the `OpenCode` logo in the left section. The right side of the header have two
buttons.

- #### Change theme

Theme icon allows you to switch between light mode and dark mode, and vice versa.

<p align="left">
<img style="padding-right: 2%;" src="../docs/images/playground_home_light_theme_screen.png" alt="light theme screen" width="45%"/>
<img style="padding-right: 2%;" src="../docs/images/playground_home_dark_theme_screen.png" alt="dark theme screen" width="45%"/>
</p>

- #### Sign-in

The "Sign-in" button opens a Google sign-in popup on the screen and prompts you to choose your email for login, with all
necessary permissions granted, including modifying ***Google Drive***.
<p align="left">
<img style="padding-right: 2%;" src="../docs/images/playground_signIn.gif" alt="Playground Sign In" width="60%" height="20%"/>
</p>

- #### Profile Options

Upon successful sign-in, you will have options to edit your profile and log out. The "Edit Profile" button opens a popup
where you can update your profile image and display name.

<p align="left">
<img style="padding-right: 2%;" src="../docs/images/playground_edit_profile_logout_popup.png" alt="Playground Sign In" width="45%"/>
<img style="padding-right: 2%;" src="../docs/images/playground_edit_profile_modal.png" alt="Playground Sign In" width="45%" />

</p>


- #### Playground page additional
    - The Playground page header retains the same design as the homepage header, while incorporating additional
      functionalities. In
      the center, the project name is displayed with a downward arrow, providing options for renaming and deleting the
      project.

         <p align="left"> 
         <img style="padding-right: 2%;margin-top: 2%" src="../docs/images/playground_workspace_rename_and_delete_options.png" alt="Playground Sign In" width="50%" height="50%" />
         </p>

    - On the right side, a help button has been added, featuring three sections that explain how to effectively
      drag and drop blocks, save and download project progress, and upload to Drive for seamless collaboration.

        <p align="left">
        <img style="padding-right: 2%;margin-top: 2%" src="../docs/images/playground_help.png" alt="Playground Help" width="50%"/>
        </p>

#### Carousal

Carousal's container explains how Application works.
<p>
<img style="padding-right: 2%;" src="../docs/images/home_carousal1.png" alt="home_carousal1" width="30%"/>
<img style="padding-right: 2%;" src="../docs/images/home_carousal2.png" alt="home_carousal2" width="30%"/>
<img style="padding-right: 2%;" src="../docs/images/home_carousal3.png" alt="home_carousal3" width="30%"/>
</p>

#### Project Section

The 'My Projects' section displays projects stored in local storage and Google Drive (if the user is signed in), with
each project showing its name, creation/edit date, and previous block versions. Clicking on a project redirects the user
to its playground page. To create a new project, simply click on the `create icon`.

Clicking the 'Create' icon opens a 'Create New Project' popup with an input for the project name and a 'Create' button.
Once a suitable name is entered and the 'Create' button or enter is pressed, the project's playground will open. If the
user enters a name already assigned to another project, the system will automatically generate a unique name by
appending an integer to the end of the name.
<p align="left">
<img style="padding-right: 2%;" src="../docs/images/create_new_project.png" alt="Create New Project" width="30%"/>
<img style="padding-right: 2%;" src="../docs/images/my_projects.png" alt="my Project" width="30%"/>
<img style="padding-right: 2%;" src="../docs/images/my_project_option.png" alt="option" width="30%"/>
</p>

#### WorkSpace

To generate code, users can drag and drop coding blocks into the workspace. The code can be converted into both
JavaScript and Python.

- Blocks can be selected from the left section and dropped into the workspace as needed.
- To delete a block, users can simply drag it to the trash can icon.
- If a block does not fit into the "Start" or "Forever" block, it will be disabled to prevent errors in the generated
  code.
- Users can restore a deleted block from the trash can by clicking on it, which will display a list of deleted blocks.
  They can then drag and drop the desired block from the trash can back into the workspace.
<p align="left">
  <img style="padding-right: 2%;" src="../docs/images/playground_workspace.gif" alt="Create New Project" width="50%"/>
</p>


#### Playground Bottom Bar

- To ensure a successful web experience of the openBot playground using Google Drive, users should fulfill the following
  conditions:
    - user should not have any other folder in their Google Drive with the same name as the website generated
      openBot-PlayGround folder.
    - user should not create same name file in openBot-PlayGround folder.
  <p align="left">
  <img style="padding-right: 2%; margin-top: 2%;" src="../docs/images/google_drive_folder.png" alt="Generate Code" width="30%"/>
  <img style="padding-right: 2%; margin-top: 2%" src="../docs/images/playground_drive_file.png" alt="Generate Code" width="50%"/>
  </p>

- #### Generate Code

Generate Code button on the Playground Bottom Bar serves two important functions. Firstly, it generates a QR code that
represents the link of the JavaScript/Python file uploaded to the user's Google Drive as part of the project. This QR
code is displayed in a side window for easy access and sharing. Secondly, the button also uploads a JavaScript/Python
file containing the code for the project to the user's Google Drive.

The QR code generated by the button provides a public, shareable link to the JavaScript/Python file on Google Drive.
This link can be accessed by scanning the QR code using the OpenBot IOS/Android app. This allows users to run the car
based on the code generated using the coding blocks directly from their mobile device. The ability to share the QR code
and access the code on mobile devices adds another level of convenience and accessibility to the openBot playground.

- #### Upload in Drive

Cloud icon upload xml file of current project block to google drive.

Here is an upload in Drive and generate Code Demo :
<p align="left">
<img style="padding-right: 2%;" src="../docs/images/playground_google_drive.gif" alt="Generate Code" width="95%"/>
</p>

- #### WorkSpace Controller

The undo-redo button helps to do undo redo functionalities in the playground.
The plus icon is for zoom-in and the minus icon is for zoom-out.