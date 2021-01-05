package tupperdate.android.ui.onboarding

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tupperdate.android.data.features.auth.PhoneRegistrationApi

val KoinModuleUIOnboarding = module {
    viewModel { (onCheckCode: () -> Unit, phone: PhoneRegistrationApi) ->
        OnboardingViewModel(
            onCheckCode = onCheckCode,
            api = phone,
        )
    }
    viewModel { (phone: PhoneRegistrationApi) -> OnboardingConfirmationViewModel(phone) }
}
