# CIS 244 Spring 2025 Group 2

From a high-level administrative perspective, we will each have a [Fork](https://docs.github.com/en/get-started/quickstart/fork-a-repo) of this repository, then use [Pull Requests](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request?tool=webui) to submit and review code (merged into our local Forks). 

The description below is an ideal of what we hope to eventually get to in order to align our projects with real-world use of GitHub:
1. `develop` will be the branch with all the latest code changes, being updated as we go (it may or may not be in a “working” state at a given time, depending on how complete merged changes are). This branch, for example, would be where we merge our changes at the end of the day so that others in the Group can see what we’ve been working on in our local repo.
2. `QA` will be the latest “stable” version. Changes in this branch will be sourced from modifications in the develop branch, once those commits have been reviewed and are ready to be added to the other completed portions of the project (to be tested with the already completed project aspects to ensure that the new changes don’t break anything previously submitted).
3. Once changes have been QA tested, they will be merged from QA into the `master` branch, which will be the “public” version of the project (i.e., a working version that can be played with the files in their current state).

### Initial Setup

Assuming that Git has already been installed on our local machines and we've created a GitHub account, the next step is to make and prepare our Fork:
1. Navigate to [GroupProject:develop](https://github.com/CIS244Spring2025Group2/GroupProject/tree/develop) in a browser window, then click the "Fork" button in the upper right and select where to fork this repository (your own user's repository, in this case).
2. While viewing the Fork in a browser page, copy the repo's SSH address (using the "Code ▾" and copy buttons)
3. Open a local terminal where the working repository will be located on your local machine (for example, in a directory called /git), then execute: `git clone [copied text]` (for example, `git clone git@github.com:madeleineclay/GroupProject.git`). You should now have a "GroupProject" directory present
4. Navigate in the terminal into this new directory, then add the remote "upstream" repo (what is shared between our Pod): `git remote add upstream git@github.com:CIS244Spring2025Group2/GroupProject.git`
5. Now we can sync the contents of our Fork and the upstream repository: 
 * Execute `git fetch upstream` (to fetch all branches and their commits from the CIS244-Spring2025-Group2 repository), then:
 * Execute `git checkout develop`  (since we're syncing our fork's develop with the original repo's develop branch)
 * Execute `git merge upstream/develop`

We should now have an "up to date" fork that can be used locally and developed.

Optional, but recommended: [edit your bash to display the current directory name](https://grow.liferay.com/people/GIT+tips+and+tricks#section-GIT+tips+and+tricks-How+to+display+the+current+branch+name+in+your+prompt+on+Linux) when using the terminal.

### Create a new branch

To checkout a different branch than `develop` (such as `master`), simply run `git checkout master`. Because this `master` branch already exists in the primary repository, this will create and switch to a new branch in our fork that will be connected to the primary repository's `master` branch. You can similarly create other new branches (if you'd like) in your personal repository by adding the -b flag; for example: `git checkout -b new-branch-name`

Note: creating a new branch is only necessary for larger projects -- for smaller projects, we can work directly on `develop`.

### Daily process
To more specifically detail our day-to-day coding steps:
1. Check to see if all pull requests have been merged prior to syncing your code: https://github.com/CIS244Spring2025Group2/GroupProject/pulls
2. In order to sync your local repository with the changes on the remote upstream repository, run:

 * `git fetch upstream`
 * `git merge upstream/develop` (assuming that work is being done on the `develop` branch).

You have now synced your local files with the remote files.

3. Once you'd like to make a commit and push your local changes to your remote origin repository, execute the following:

* `git status`
  * This will verify that we’re working on the correct branch, and what files have been modified that will or will not be committed (file names in red will not be committed, green will be committed)
* `git add .` [alternately: `git add filename` to only stage specific files]
  * This will prepare/stage all files (with `.`) listed in the status command to be committed
* `git status`
  * Now files should be listed to be included within a commit
* `git commit -m “Message about changes”`
  * This will create the “commit” (with its individual hash), using any files that we’ve staged.
* `git push origin develop`
  * This is the command that will upload our local repository content (the commit changes) to the remote repository (our Fork on GitHub)

At this point, you can check your Fork on GitHub and should see a note that says something like “This branch is 1 commit ahead of [GroupProject:develop](https://github.com/CIS244Spring2025Group2/GroupProject).”

4. Now that changes are on your personal GitHub account, you can create a pull request at https://github.com/CIS244Spring2025Group2/GroupProject/pulls requesting that your changes be merged into upstream. Make sure you are using the correct branch in your pull request and double check the files that it detects as being changed. These changes will be reviewed and merged at the beginning of each coding session.

Note: After the PR is merged, you may see a "this branch is 1 commit behind" message when viewing your Fork in GitHub. This can be ignored, as since our Fork did not have the action of merging in our pushed changes, the message is expected. However, to resolve the messages we can run the following (replacing the branch name for the one being aligned):
* `git checkout master`
* `git pull upstream master`
* `git push origin master`

### Troubleshooting references
1. Cloning the repository: https://help.github.com/en/articles/cloning-a-repository
2. Connect local repo to remote repo: https://gist.github.com/mindplace/b4b094157d7a3be6afd2c96370d39fad#connect-your-local-project-folder-to-your-empty-folderrepository-on-github
3. Basic Git commands: https://product.hubspot.com/blog/git-and-github-tutorial-for-beginners
4. If you receive errors related to keys for pushing to the repo: https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent and https://help.github.com/en/articles/adding-a-new-ssh-key-to-your-github-account