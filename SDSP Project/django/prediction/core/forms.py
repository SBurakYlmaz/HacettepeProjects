import joblib
from crispy_forms.helper import FormHelper
from crispy_forms.layout import Submit
from django import forms

export = joblib.load('export.pkl')


class DynamicModelForm(forms.Form):
    added_feature_list = []
    for feature in export['model_features']:
        if feature.count('_') > 1:
            feature = feature.split('_')[0] + "_" + feature.split('_')[1]
            if feature not in added_feature_list:
                added_feature_list.append(feature)
                categories = "[('', 'Choose...'),"
                for category in export['categorical_data'][feature]:
                    categories = categories + "(\"" + category + "\",\"" + category + "\"),"
                categories = categories[:-1] + "]"
                exec(feature + "= forms.ChoiceField(choices=" + categories + ")")
        else:
            if export['form_data'][feature] == "yes-no":
                added_feature_list.append(feature)
                exec(
                    feature + "= forms.ChoiceField(choices=(('Yes','Yes'),('No','No')), widget=forms.RadioSelect, initial='Yes')")
            elif export['form_data'][feature] == "float64":
                added_feature_list.append(feature)
                exec(feature + "=forms.FloatField(help_text='Give Float')")
            elif export['form_data'][feature] == "int64":
                added_feature_list.append(feature)
                exec(feature + "=forms.IntegerField(help_text='Give Integer')")
            else:
                added_feature_list.append(feature)
                categories = "[('', 'Chosee...'),"
                for category in export['categorical_data'][feature]:
                    categories = categories + "(\"" + category + "\",\"" + category + "\"),"
                categories = categories[:-1] + "]"
                exec(feature + "= forms.ChoiceField(choices=" + categories + ")")

    def __init__(self, *args, **kwargs):
        super(DynamicModelForm, self).__init__(*args, **kwargs)
        self.helper = FormHelper()
        self.helper.add_input(Submit('submit', 'Submit'))

    def submit(self):
        predict = []
        scaler = export['scaler']
        model = export['model']

        feature_index = 0
        model_features = export['model_features']
        for field in self.fields.keys():
            if model_features[feature_index].count('_') > 1:
                while model_features[feature_index].count('_') > 1:
                    if model_features[feature_index].split('_')[2] != self[field].value():
                        predict.append(0)
                    else:
                        predict.append(1)
                    feature_index += 1
            else:
                if export['form_data'][model_features[feature_index]] == "yes-no":
                    if self[field].value() == 'Yes':
                        predict.append(1)
                    else:
                        predict.append(0)
                elif (export['form_data'][model_features[feature_index]] == "float64") or (
                        export['form_data'][model_features[feature_index]] == "int64"):
                    predict.append(self[field].value())
                else:
                    if self[field].value() == 'Female':
                        predict.append(1)
                    else:
                        predict.append(0)
                feature_index += 1
        predict = scaler.transform([predict])
        predict_proba = model.predict_proba(predict)
        sorted_predict_proba = {}
        for disease_name, prob in zip(export['categorical_data']['Disease'], list(predict_proba[0])):
            sorted_predict_proba[disease_name] = int(round(prob * 100, 0))
        sorted_predict_proba = sorted(sorted_predict_proba.items(), key=lambda kv: kv[1], reverse=True)
        return sorted_predict_proba[:3]
