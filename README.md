# Parkour

[![Jenkins](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.jcx.ovh%2Fjob%2FDumbDogDiner%2Fjob%2Fparkour%2Fjob%2Fstable%2F&label=jenkins%20%7C%20stable&logo=jenkins&logoColor=white)](https://ci.jcx.ovh/job/DumbDogDiner/job/parkour/job/stable/)
[![Jenkins](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.jcx.ovh%2Fjob%2FDumbDogDiner%2Fjob%2Fparkour%2Fjob%2Fdev%2F&label=jenkins%20%7C%20dev&logo=jenkins&logoColor=white)](https://ci.jcx.ovh/job/DumbDogDiner/job/parkour/job/dev/)

Interactive Minecraft Parkour plugin with a personality.

## Features
- Course Editor - create courses by using the `/pk create <name>` command, and use the editor to add checkpoints to the course. Once finished, drop the editor, and the course will be finalized.
- Personal bests & server records - once players complete the course for the first time, they will set a personal best. Beating this, and the overall server best, displays special messages in chat.

## Commands
**Requires:** `default`
`/pk quit` - stop the current parkour session (both physical and the editor).

**Requires:** `parkour.command`
`/pk create <name>` - create a new course with the editor.
`/pk delete <name>` - delete the named course.
`/pk list` - list all available courses.

## Placeholders
Parkour has a few placeholders for use with PlaceholderAPI.

- `parkour_course_<name>_record` - the fastest time this course has been completed in. Given in seconds.
- `parkour_course_<name>_personal_best` - the fastest time the executing player has completed this course in. Given in seconds.
- `parkour_course_<name>_checkpoint_count` - the number of checkpoints in this course.

## Next Steps
- Bounding boxes - areas which reset players back to the previous checkpoint if they leave without passing the next checkpoint.
- Parkour tools - controls with which players can jump back to previous checkpoints, or the start of the course.
- Effect pads - grant players who step on them potion effects for a wider variety of possible courses.
- Jump pads - launch players who step on them into the air, controllable with a sexy editor owo~
- Vault integration - allow the giving of currency via vault when players step on checkpoints and complete courses.
- Coins - balls of particles that act as goals that players obtain in exchange for currency.
- Par Times - configuration of the maximum time of completion before rewards begin to fall off.
