module Main exposing (main)

import Browser exposing (Document, UrlRequest(..))
import Browser.Navigation exposing (Key)
import Html exposing (Html, div, h1, li, p, text, ul)
import Html.Attributes exposing (class, href)
import Json.Decode as D
import Url exposing (Url)



--- MAIN ---


main : Program D.Value Model Msg
main =
    Browser.application
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        , onUrlRequest = onUrlRequest
        , onUrlChange = onUrlChange
        }



--- MODEL ---


type Model
    = Empty


init : D.Value -> Url -> Key -> ( Model, Cmd Msg )
init _ _ _ =
    ( Empty, Cmd.none )


onUrlRequest : UrlRequest -> Msg
onUrlRequest urlRequest =
    ClickedLink urlRequest


onUrlChange : Url -> Msg
onUrlChange url =
    ChangedUrl url



--- UPDATE ---


type Msg
    = ClickedLink UrlRequest
    | ChangedUrl Url


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case ( msg, model ) of
        ( _, _ ) ->
            ( model, Cmd.none )



--- VIEW ---


view : Model -> Document Msg
view _ =
    let
        title =
            "Tupperdate.me"

        body =
            [ viewLanding
            ]
    in
    { title = title, body = body }


viewLanding : Html Msg
viewLanding =
    div
        [ class "flex flex-row"
        ]
        [ tupperDate
        , div
            [ class "w-1/2 h-screen"
            , class "p-16 text-right"
            , class "text-white font-archivo"
            , class "bg-smurf-800"
            ]
            [ h1
                [ class "m-auto"
                , class "text-6xl font-bold font-archivo"
                , class "border-b-4 border-white"
                ]
                [ text "Who are we ?" ]
            , ul
                [ class "m-4 text-4xl font-archivo"
                ]
                [ li [] [ text "Alexandre Piveteau" ]
                , li [] [ text "Matthieu Burguburu" ]
                , li [] [ text "David Dupraz" ]
                , li [] [ text "Guy-Laurent Subri" ]
                ]
            , p
                [ class "m-4" ]
                [ text "Check us out on "
                , Html.a
                    [ href "https://github.com/heig-PDG/mono"
                    , class "underline"
                    ]
                    [ text "GitHub" ]
                ]
            ]
        ]


tupperDate : Html Msg
tupperDate =
    div
        [ class "w-full h-screen"
        , class "font-archivo"
        , class "flex flex-col"
        , class "bg-white"
        ]
        [ div
            [ class "m-auto" ]
            [ h1
                [ class "w-full m-auto"
                , class "text-6xl font-bold text-center"
                , class "text-white"
                , class "customTitleGradient"
                ]
                [ text "Tupper • Date" ]
            , p
                [ class "w-full px-16 mx-auto my-8"
                , class "text-2xl text-center font-archivo"
                ]
                [ text "Discover folks who cook what you like"
                , Html.br [] []
                , text "and start sharing meals with them today."
                ]
            ]
        ]



-- SUBSCRIPTIONS ---


subscriptions : Model -> Sub Msg
subscriptions _ =
    Sub.none
