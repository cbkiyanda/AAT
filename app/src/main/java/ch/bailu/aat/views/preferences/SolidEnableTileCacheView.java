package ch.bailu.aat.views.preferences;

/*
public class SolidEnableTileCacheView extends LinearLayout {

    private final Activity acontext;

    public SolidEnableTileCacheView(Activity ac, SolidEnableTileCache s) {
        super(ac);

        acontext = ac;
        addW(new SolidCheckBox(s));
        addView(new SolidEmptyCacheButton(s));
    }


    public void addW(View v) {
        addView(v);

        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) v.getLayoutParams();
        l.weight = 1;
        v.setLayoutParams(l);
    }


    public class SolidEmptyCacheButton extends ImageButtonView {
        public SolidEmptyCacheButton(final SolidEnableTileCache s) {
            super(s.getContext(), R.drawable.user_trash_inverse);

            final String getName = s.getKey();
            final File file = getCacheDirectory(getName);

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AppDialog() {
                        @Override
                        protected void onPositiveClick() {
                            deleteRecursive(file);
                            AppLog.i(getContext(), "Removed* " + file.getAbsolutePath());
                        }
                    }.displayYesNoDialog(
                            acontext,
                            "Empty cache*",
                            "Remove all files in* " + file.getAbsolutePath());

                }
            });
        }




        public File getCacheDirectory(String getName) {
            return new File(
                    new SolidTileCacheDirectory(getContext()).getValueAsFile(),
                    getName);
        }

        public void deleteRecursive(File fileOrDirectory) {
            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    deleteRecursive(child);

            fileOrDirectory.delete();
        }
    }

}
*/