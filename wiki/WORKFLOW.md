# Workflow

This document provides an overview of the collaboration principles that drive our project. It includes information relative to the contribution workflow, the structure of meetings and the organization of sprints and epics (iterations).

> No document is auto-sufficient or frozen over time. This is also the case with the workflow of our team, which we will update as time goes by.

## Contributing

We follow an approach inspired by the [Rapid Cycle Workflow](https://gitlab.com/softeng-heigvd/teaching-heigvd-pdg-2020/guidelines/-/blob/master/WORKFLOW.md). We adapt it to match our specific use-case:

- we're using GitHub, in order to centralize all of our services (issues, project management, milestones, hosting, continuous integration);
- we want to make systematic pull request reviews; and
- we'll be using an external time tracking system.

### Rapid Cycle Workflow

1. Based on what was agreed on during meetings, file an issue for a task you'd like to accomplish. When choosing a task, choose something that you think should take you **less than 4 hours** to achieve. It's better to aim for a task that's too small rather than a task that's too big !
	+ Use the dedicated issue template to file that task.
	+ Fill in our time tracking system the expected duration of this task.
2. Use your personal fork, and create a branch dedicated to the issue. Create a [draft pull request](https://github.blog/2019-02-14-introducing-draft-pull-requests/) as soon as you can (if possible, this should be on your first commit).
3. Whenever you commit your code, log the amount of time that you have spent on your task. Try to be accurate; it will help us estimate future tasks more precisely !
	+ Follow [this article](https://chris.beams.io/posts/git-commit/) on how to write a good commit messages. The Git history of your contribution will also be looked at during the review.
	+ Add a description to your commit message if relevant.
	+ When doing some peer coding, [create a commit with multiple authors](https://docs.github.com/en/enterprise/2.13/user/articles/creating-a-commit-with-multiple-authors).
	+ If you have a question on your work, write it in the feed of the draft pull request.
4. Once you are done with your contribution, mark your draft as ready. Make sure that merging the pull request will close the issue created in `1.` !
	+ To do that, you should probably write in the pull request body that it will `Fix issue #3.`.
5. Add two reviewers to your pull request (including the colleague working in your "team").
6. If the review is a success and the integration and unit tests pass, **merge your pull request** and celebrate 🍾


## Meetings

> We make heavy use of the team meetings plan we implemented together [in the PRO class](https://github.com/heig-PRO-b04/java-backend/blob/master/wiki/Meetings.md).

Team alignments are organized **two times a week**. They occur:

+ on **Tuesday at 10 AM**; and
+ on **Friday at 8:45 AM**.

If you can't be there on time, please let us know about it 😄 We chose this schedule because we get to see each other for in-person classes on Wednesday and Thursday. If relevant, we might change the team meetings timing.

### Goals and non-goals

These regular team alignments have very specific goals, and there are a few important non-goals and traps to avoid. At the end of the meeting :

+ Everyone should be able to (vaguely) explain what his/her teammates will be working on until the next group alignment.
+ Anyone with a technical or organizational blocker will have been able to share it with his/her teammates, and will have been able to plan a one-to-one chat with whoever can help them solve their issue.
+ **Non-goal :** Team alignments must not replace technical discussions. They are aimed at solving organizational problems, not technical hiccups.


### Structure

There are a lot of ways to run team meetings. The following structure is proposed, but it may vary as needed (and should therefore be considered as a set of recommendations rather than mandatory points).

1. Everyone gets to present what they've been working on since the last meeting (or skip if there isn't anything to show).
	+ If you plan to showcase something, please make sure to have it working and running before !
2. Once everyone has showed what is working, a second round is done with eventual blockers that members might have.
	+ If you need help on something, this is when you should tell us about it ! Someone else will be designated to give you a hand.
3. Once blockers have been adressed, a last round is planned to let you tell us about what you plan to be working on until the next meeting. This is the ideal time for open discussions if you don't know what to work on next, or feel like we need to plan future work differently.
	+ You're not committing to work on what you're talking about; we're all adults, and you probably know well what the priorities of your part of the project are. This round is just meant to indicate to your teammates what they should expect to see changing on the project in the next 2-3 days.

### Sprints (and the Tuesday meeting)

In general, we will try to organize our work around sprints of 7 days, starting and ending on Tuesday. This gives us some time to do some in-depth work, while still letting us do enough iterations before the project deadline. Every week, on Tuesday:

+ we make a retrospective on the work done during the last week; (10 mins)
+ we collaboratively define the objectives for the next week; (15-20 mins)
+ we create a new milestone on GitHub; and
+ we add and tag all the issues related to the sprint with the milestone.

We use a Kanban board to keep track of all the issues currently in progress.