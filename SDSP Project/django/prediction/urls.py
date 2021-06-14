from django.urls import path

from prediction.core import views

urlpatterns = [
    path('', views.MainPage.as_view(template_name='form.html'), name='form')
]
