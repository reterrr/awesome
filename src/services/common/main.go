package common

type App interface{}

type Service struct {
	Config *Config
	App    *App
}

var port string

func NewService(config *Config, app *App) *Service {
	return &Service{
		Config: config,
		App:    app,
	}
}

func buildConnectionString() string {

}

func (s *Service) Run() {

}
