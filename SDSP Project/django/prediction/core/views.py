from django.shortcuts import render
from django.urls import reverse_lazy
from django.views.generic import FormView

from .forms import DynamicModelForm


class MainPage(FormView):
    form_class = DynamicModelForm
    success_url = reverse_lazy('form')

    def form_valid(self, form):
        disease_probabilities = form.submit()
        response = {'form': form, 'disease_probabilities': disease_probabilities}
        super().form_valid(form)
        return render(self.request, 'form.html', response)
