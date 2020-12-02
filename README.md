# heig-PDG/mono

![Build and publish Android app to Telegram](https://github.com/heig-PDG/mono/workflows/Build%20and%20publish%20Android%20app%20to%20Telegram/badge.svg)
![Build and publish Docker image](https://github.com/heig-PDG/mono/workflows/Build%20and%20publish%20Docker%20image/badge.svg)
![Deploy Website to GitHub Pages](https://github.com/heig-PDG/mono/workflows/Deploy%20Website%20to%20GitHub%20Pages/badge.svg)
[![Heroku App Status](http://heroku-shields.herokuapp.com/heig-pdg)](https://heig-pdg.herokuapp.com)

An Android and web application to match and meet people based on meals, recipes and tupperwares. This is a semester project done at HEIG-VD. More information is available on the [class repository on GitLab](https://gitlab.com/softeng-heigvd/teaching-heigvd-pdg-2020/guidelines).

## Team

| Name                                   |                                  |
|----------------------------------------|----------------------------------|
| Matthieu Burguburu 					 | matthieu.burguburu@heig-vd.ch    |
| David Dupraz                           | david.dupraz@heig-vd.ch          |
| Alexandre Piveteau 				     | alexandre.piveteau@heig-vd.ch    |
| Guy-Laurent Subri                      | guy-laurent.subri@heig-vd.ch     |

## Structure

We use a mono-repository approach to store and manage the files of our project. We also make use of some external tools for [time tracking](https://clockify.me) and [prototyping](https://www.figma.com).

The sub-folder structure is as follows:

- `.github/` contains GitHub-specific files, such as issue templates, and the workflows that run whenever we make changes to the app;
- `docker` contains our different docker images, and our Docker-related scripts;
- `spec/` contains our [OpenAPI spec](http://spec.tupperdate.me),
- `src/` contains the source code of our application;
- `vcs` contains some scripts related to our VCS;
- `website/` contains our landing page; amd
- `wiki/` contains our reference branding as well as details over our collaboration process.

**Our prototypes are visible on Figma ([overview](https://www.figma.com/file/sZTqSZOMoUmuJTMhw7khx3/Mobile?node-id=1%3A2), [live prototype](https://www.figma.com/proto/sZTqSZOMoUmuJTMhw7khx3/Mobile?node-id=158%3A380&viewport=849%2C581%2C0.3042562007904053&scaling=scale-down)).**

## Time tracking

Since we're using Clockify to track the time for the project, we thought that it'd be useful to monitor the time invested every week.

### Overview

This table represents the total time invested. It will be updated two times a week.

| Date | Total time |
| :--: | :-----: |
| 2020-10-02 | 12h 22m 34s |
| 2020-10-06 | 19h 55m 55s |
| 2020-10-06 | 21h 35m 47s |
| 2020-10-09 | 24h 49m 34s |
| 2020-10-13 | 24h 49m 34s |
| 2020-10-16 | 27h 59m 13s |
| 2020-10-20 | 35h 14m 13s |
| 2020-10-23 | 35h 14m 13s |
| 2020-10-27 | 40h 47m 13s |
| 2020-10-30 | 40h 47m 13s |
| 2020-11-03 | 40h 47m 13s |
| 2020-11-06 | 45h 53m 46s |
| 2020-11-10 | 49h 53m 46s |
| 2020-11-13 | 52h 18m 10s |
| 2020-11-17 | 53h 18m 10s |
| 2020-11-20 | 60h 1m 10s |
| 2020-11-24 | 115h 6m 49s |
| 2020-11-27 | 107h 43m 49s |
| 2020-12-01 | 107h 43m 49s |
