package tupperdate.android.ui.onboarding

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tupperdate.android.data.features.auth.PhoneRegistration

val KoinModuleUIOnboarding = module {
    viewModel { (onCheckCode: () -> Unit, phone: PhoneRegistration) ->
        OnboardingViewModel(
            onCheckCode = onCheckCode,
            api = phone,
        )
    }
    viewModel { (phone: PhoneRegistration) -> OnboardingConfirmationViewModel(phone) }
}
