import { inject } from "vue";

export function useSwal() {
  const swal = inject("swal");
  if (!swal) {
    throw new Error("swal is not provided");
  }
  return swal;
}
