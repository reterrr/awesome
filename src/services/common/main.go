package common

type Config interface{}

type App struct{}

type Service struct {
	config *Config
	app    *App
}

func (s Service) Initialize(config *Config, app *App) {

}

func (s Service) Run() {

}
