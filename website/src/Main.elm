module Main exposing (landingTwo, main)

import Browser exposing (Document, UrlRequest(..))
import Browser.Navigation as Nav exposing (Key)
import Html exposing (Html, div, h1, img, li, p, span, text, ul)
import Html.Attributes exposing (class, href, src)
import Icons
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
        ( ClickedLink urlRequest, _ ) ->
            performUrlRequest urlRequest model

        ( _, _ ) ->
            ( model, Cmd.none )


performUrlRequest : UrlRequest -> Model -> ( Model, Cmd msg )
performUrlRequest request model =
    case request of
        Internal _ ->
            ( model, Cmd.none )

        External url ->
            ( model, Nav.load url )



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
        [ class "flex flex-col"
        , class "items-stretch"
        ]
        [ tupperDate
        , landingOne
        , landingTwo
        , landingThree
        , landingFour
        , whoWeAre
        ]


tupperDate : Html Msg
tupperDate =
    div
        [ class "w-full"
        , class "h-screen lg:min-h-full"
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


leftContent : String -> List (Html Msg) -> Html Msg
leftContent image content =
    div
        [ class "w-full overflow-x-hidden"
        , class "h-screen"
        , class "-mt-56"
        , class "font-archivo"
        , class "flex flex-row"
        , class "items-center justify-center"
        ]
        [ img
            [ src image
            , class "funcImg"
            , class "-ml-20 md:m-0"
            ]
            []
        , p
            [ class "text-2xl text-center font-archivo"
            , class "m-4 md:m-8 md:ml-56"
            ]
            content
        ]


rightContent : String -> List (Html Msg) -> Html Msg
rightContent image content =
    div
        [ class "w-full overflow-x-hidden"
        , class "-mt-56"
        , class "h-screen"
        , class "font-archivo"
        , class "flex flex-row"
        , class "items-center justify-center"
        ]
        [ p
            [ class "text-2xl text-center font-archivo"
            , class "m-4 md:m-8 md:mr-56"
            ]
            content
        , img
            [ src image
            , class "funcImg"
            , class "-mr-20 md:m-0"
            ]
            []
        ]


landingOne : Html Msg
landingOne =
    leftContent "/assets/One.png"
        [ text "Browse and match"
        , Html.br [] []
        , text "recipes you "
        , span [ class "font-bold fastTitleGradient" ] [ text "love!" ]
        ]


landingTwo : Html Msg
landingTwo =
    rightContent "/assets/Two.png"
        [ text "Add your own Tupp's"
        , Html.br [] []
        , text "and share them with the "
        , span [ class "font-bold fastTitleGradient" ] [ text "world!" ]
        ]


landingThree : Html Msg
landingThree =
    leftContent "/assets/Three.png"
        [ text "Stay in "
        , span [ class "font-bold fastTitleGradient" ] [ text "touch" ]
        , Html.br [] []
        , text "with your new friends!"
        ]


landingFour : Html Msg
landingFour =
    rightContent "/assets/Four.png"
        [ text "Reflect who"
        , Html.br [] []
        , text "you are"
        , Html.br [] []
        , text "with your "
        , span [ class "font-bold fastTitleGradient" ] [ text "recipes!" ]
        ]


whoWeAre : Html Msg
whoWeAre =
    div
        [ class "w-full"
        , class "h-auto"
        , class "p-16 text-right"
        , class "text-white font-archivo"
        , class "bg-smurf-800"
        ]
        [ h1
            [ class "m-auto"
            , class "text-4xl font-bold md:text-5xl font-archivo"
            , class "border-b-4 border-white"
            ]
            [ text "Who we are" ]
        , ul
            [ class "m-4 text-2xl md:text-3xl font-archivo"
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
                [ text "GitHub"
                , span [ class "ml-2" ] [ Icons.github ]
                ]
            , Html.br [] []
            , text "or check out the demo "
            , Html.a
                [ href "https://demo.tupperdate.me"
                , class "underline"
                ]
                [ text "here"
                , span [ class "ml-2" ] [ Icons.figma ]
                ]
            ]
        ]



-- SUBSCRIPTIONS ---


subscriptions : Model -> Sub Msg
subscriptions _ =
    Sub.none
