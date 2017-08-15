# Pre-work - *2Doo App*

**2Doo App** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Dipen**

Time spent: **20hr** hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] User can **successfully add and remove items** from the todo list
* [X] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [X] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [ ] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [X] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [X] Add support for completion due dates for todo items (and display within listview item)
* [X] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [X] Add support for selecting the priority of each todo item (and display in listview item)
* [X] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [X] Added Intent and Bundle for passing data between two activity. 
* [X] Added Counter to display total no. of items in list.
* [X] Added Checks when user tries to close a updated screen without saving changes.  


## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://imgur.com/LU4TK1E.gif' title='2Doo App' width='600' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."


**Answer:** I have used .Net's WPF for developing UI before and it was very similar although some changes with Actvity and Fragment and how data is passed around.Overall good experience.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** Adapter is the connection between the view i.e how it should look and feel and the data that goes in that layout. It is very important since its the binding that connects view with data and essential since instead of going through each item in view you can upate all at once through adapter. ConvertView allow to reuse the old view for showing new data.i.e if we had 5 items in list and the view can show only 2 items at a time.When we scroll down isntead of recreating the view it resued the old view to add new data to it. This is use of convertview. 

## Notes

Describe any challenges encountered while building the app.
I had challenges with updating view after item edit. I have a defect in app where in after editing the text and going back to main activity the text position changes. I believe its in Adapter class where it is redrawing the list view but need to further dig into it. 

## License

    Copyright [2017] [dipen rana]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.