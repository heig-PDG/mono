#!/bin/env python3
import datetime
import math
import requests
import os

apiKey = os.environ["CLOCKIFY_API_KEY"]


def secondsToHMS(seconds):
    h = math.floor(seconds / 3600)
    m = math.floor((seconds - h*3600)/60)
    s = round(seconds % 60)

    return str(h) + "h " + str(m) + "m " + str(s).zfill(2) + "s"


def getTotalTime():
    workspaceId = "5f6457191513b10368369baf"

    apiHeader = {'X-Api-Key': apiKey}

    server = "https://reports.api.clockify.me/v1"
    endpoint = "/workspaces/" + workspaceId + "/reports/summary"

    endDate = str(datetime.datetime.now().isoformat()) + "Z"

    data = {"dateRangeStart": "2020-08-01T00:00:00.000Z",
            "dateRangeEnd": endDate,
            "summaryFilter": {
                "groups": [
                    "USER",
                    "PROJECT",
                    "TIMEENTRY"
                    ]
                }
            }

    res = requests.post(server + endpoint, json=data, headers=apiHeader)

    if res.status_code != 200:
        quit()

    totalseconds = res.json()["totals"][0]["totalTime"]
    return secondsToHMS(totalseconds)


def toMd(time):
    date = str(datetime.date.today())

    print("| " + date + " | " + time + " |")


toMd(getTotalTime())
